package com.atguigu.gmall.product.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

/**
 * 分页拦截器配置类
 */
@Configuration
public class MybatisPulsConfig {
    @Bean
    public MybatisPlusInterceptor interceptor(){
        //1：mybatisplus:总插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //2:分页下小插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        //2.1:设置页码溢出后，页永远展示最后一页
        paginationInnerInterceptor.setOverflow(true);
        //3：添加进总插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

    }

