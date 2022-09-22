package com.atguigu.gmall.order.service.impl;

import java.math.BigDecimal;

import com.atguigu.gmall.common.auth.AuthUtils;
import com.atguigu.gmall.common.constant.StyRedisConst;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.model.enums.OrderStatus;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.to.OrderMsg;
import com.atguigu.gmall.model.vo.user.UserAuthInfo;
import com.atguigu.gmall.order.service.OrderDetailService;
import com.atguigu.gmall.rabbit.constant.MqConstant;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.atguigu.gmall.model.activity.CouponInfo;


import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mengxueshong
 * @description 针对表【order_info(订单表 订单表)】的数据库操作Service实现
 * @createDate 2022-09-18 21:46:01
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
        implements OrderInfoService {
    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     *  4:幂等修改订单状态：
     * @param orderId
     * @param userId
     * @param closed
     * @param expected
     */
    @Override
    public void changeOrderStatus(Long orderId, Long userId, ProcessStatus closed, List<ProcessStatus> expected) {
    //1:获取当前最终的状态：订单状态：处理状态
        String orderStatus = closed.getOrderStatus().name();
        String processStatus = closed.name();
        // 2：stream流拿到期望状态
        List<String> expectStatus = expected.stream().map(status -> status.name()).collect(Collectors.toList());
        //3: 修改数据库的订单状态：
        orderInfoMapper.updateorderStatus(orderId,userId,processStatus,orderStatus,expectStatus);

    }
    /**
     * 3:订单详情的数据
     * @param orderSubmitVo
     * @param orderInfo
     * @return
     */
    private List<OrderDetail> prepareOrderDetail(OrderSubmitVo orderSubmitVo, OrderInfo orderInfo) {

        List<OrderDetail> detailList = orderSubmitVo.getOrderDetailList().stream()
                .map(vo -> {
                    OrderDetail detail = new OrderDetail();
                    //订单id
                    detail.setOrderId(orderInfo.getId());
                    //skuId
                    detail.setSkuId(vo.getSkuId());
                    //用户id
                    detail.setUserId(orderInfo.getUserId());

                    detail.setSkuName(vo.getSkuName());
                    detail.setImgUrl(vo.getImgUrl());
                    detail.setOrderPrice(vo.getOrderPrice());
                    detail.setSkuNum(vo.getSkuNum());
                    detail.setHasStock(vo.getHasStock());
                    detail.setCreateTime(new Date());
                    detail.setSplitTotalAmount(vo.getOrderPrice().multiply(new BigDecimal(vo.getSkuNum() + "")));
                    detail.setSplitActivityAmount(new BigDecimal("0"));
                    detail.setSplitCouponAmount(new BigDecimal("0"));
                    return detail;
                }).collect(Collectors.toList());
        return detailList;
    }
    /**
     * 2: 封装： order Info 数据
     *
     * @param orderSubmitVo
     * @return
     */
    private OrderInfo prepareOrderInfo(OrderSubmitVo orderSubmitVo, String tradeNo) {


        // 1: 创建orderinfo 对象
        OrderInfo orderInfo = new OrderInfo();
        // 2: 封装：
        // 2.1:收件人
        orderInfo.setConsignee(orderSubmitVo.getConsignee());
        // 2.2: 收件人的电话
        orderInfo.setConsigneeTel(orderSubmitVo.getConsigneeTel());
        // 2.3: 总金额：总价：
        // 2.31：得到总价
        BigDecimal totalAmount = orderSubmitVo.getOrderDetailList().stream()
                .map(o -> o.getOrderPrice().multiply(new BigDecimal(o.getSkuNum() + "")))
                .reduce((o1, o2) -> o1.add(o2)).get();

        orderInfo.setTotalAmount(totalAmount);

        // 2.4:订单状态
        orderInfo.setOrderStatus(OrderStatus.UNPAID.name());

        //  2.5:用户id
        Long userId = AuthUtils.getCurrentAuthInfo().getUserId();
        orderInfo.setUserId(userId);
        // 2.6:支付方式
        orderInfo.setPaymentWay(orderSubmitVo.getPaymentWay());

        // 2.7:物流地址：
        orderInfo.setDeliveryAddress(orderSubmitVo.getDeliveryAddress());
        // 2.8: 提交备注内容
        orderInfo.setOrderComment(orderSubmitVo.getOrderComment());
        // 2.9: 对外流水号
        orderInfo.setOutTradeNo(tradeNo);
        // 3.0: 交易体：拿到这个订单中购买的第一个商品的名字：作为订单的体
        orderInfo.setTradeBody(orderSubmitVo.getOrderDetailList().get(0).getSkuName());
        // 3.1:创建时间：
        orderInfo.setCreateTime(new Date());
        // 3.3:订单过期时间
        orderInfo.setExpireTime(new Date(System.currentTimeMillis() + 1000 * StyRedisConst.ORDER_EXPIRETIME));
        // 3.4 :订单进入状态：未支付
        orderInfo.setProcessStatus(ProcessStatus.UNPAID.name());
        // 3.5: 物流号： 发货后才有
        orderInfo.setTrackingNo("");
        // 3.6:  父订单id
        orderInfo.setParentOrderId(0L);
        // 3.7: 照片；
        orderInfo.setImgUrl(orderSubmitVo.getOrderDetailList().get(0).getImgUrl());
        // 3.8 :订单详情单独抽取一个方法 ：封装：
//        orderInfo.setOrderDetailList(Lists.newArrayList());

        // 4.0: 活动后总金额
        orderInfo.setActivityReduceAmount(new BigDecimal("0"));
        //当前单被优惠券减掉的金额
        orderInfo.setCouponAmount(new BigDecimal("0"));

        // 4.1   原始金额：
        orderInfo.setOriginalTotalAmount(totalAmount);
        // 4.2:  可退款日期：30天
        orderInfo.setRefundableTime(new Date(System.currentTimeMillis() + StyRedisConst.ORDER_REFUNDTIME * 1000));
        // 4.3:第三方物流品台，动态计算运算
        orderInfo.setFeightFee(new BigDecimal("0"));
        orderInfo.setOperateTime(new Date());

        return orderInfo;
    }

    /**
     * 1:保存confirm提交的数据：到数据库
     *
     * @param orderSubmitVo
     * @return
     */
    @Transactional
    @Override
    public Long saveSubmitOrderData(OrderSubmitVo orderSubmitVo,String tradeNo) {
        // 1:准备 订单 的数据：
        OrderInfo orderInfo = prepareOrderInfo(orderSubmitVo,tradeNo);

        // 2:封装：order：detail：的数据：
          List<OrderDetail> details =prepareOrderDetail(orderSubmitVo,orderInfo);
        // 3: orderDetail：保存到数据库
        orderDetailService.saveBatch(details);



          // 4:保存：orderInfo 保存到数据库
        orderInfoMapper.insert(orderInfo);

        // 4.1:保存了数据库：给交换机发送消息
        /**
         *  知识点： mq中 发送消息：会是转换为流的方式
         *     为了全局统一性： 转换为json 数据
         */
        OrderMsg orderMsg = new OrderMsg(orderInfo.getId(), orderInfo.getUserId());
        rabbitTemplate.convertAndSend(MqConstant.EXCHANGE_ORDER_EVENT,
                MqConstant.RK_ORDER_CREATE,
                Jsons.toStr(orderMsg));



        // 3:返回订单的id
        return orderInfo.getId();
    }


}




