package com.atguigu.gmall.common.handler;

import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理类
 *
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        // 1:改造 系统的全局异常 获取到失败的 result.fail
        Result<Object> result = Result.fail();
           // 2:  e.异常数据 通过result message方法返回
        return result.message(e.getMessage());
    }

    /**
     * 自定义异常处理方法
     * @param e
     * @return
     */
    @ExceptionHandler(GmallException.class)
    @ResponseBody
    public Result error(GmallException e){
         // 1:获取 失败：
        Result<Object> fail = Result.fail();
        // 2:对fail改造：
        fail.setCode(e.getCode());
        fail.setMessage(e.getMessage());
        return fail;
    }
}
