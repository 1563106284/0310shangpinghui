package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.config.MinioProperties;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
* @author mengxueshong
* @description 针对表【base_trademark(品牌表)】的数据库操作Service实现
* @createDate 2022-08-23 10:13:45
*/
@Service
public class BaseTrademarkServiceImpl extends ServiceImpl<BaseTrademarkMapper, BaseTrademark>
    implements BaseTrademarkService {

    @Autowired
    MinioProperties minioProperties;

    @Autowired
    MinioClient minioClient;

    @Override
    public String uploadFile(MultipartFile file) throws Exception {

        //3：优化 文件名，防止文件被覆盖 +设置时间
        String fileName = System.currentTimeMillis() + UUID.randomUUID().toString();

        //4:配置上传
        minioClient.putObject(
                PutObjectArgs.builder().bucket(minioProperties.getBucketName()).object(fileName).stream(
                                file.getInputStream(), file.getSize(), -1L)
                        .contentType(file.getContentType())
                        .build());

        //上传后的地址
        String url = minioProperties.getEndpoint() + "/" + minioProperties.getBucketName() + "/" + fileName;




            return url;

        }

    }


    /*

*/



