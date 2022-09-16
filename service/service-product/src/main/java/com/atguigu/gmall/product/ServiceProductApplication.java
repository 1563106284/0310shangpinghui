package com.atguigu.gmall.product;


import com.atguigu.gmall.common.annotation.EnableThreadPool;
import com.atguigu.gmall.common.config.RedissonAutoConfiguration;
import com.atguigu.gmall.common.config.Swagger2Config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

//包扫描：
@EnableFeignClients(basePackages = "com.atguigu.gamll.feign.search")
@EnableScheduling   // 开启定时任务的自动调度
@Import({Swagger2Config.class})
@EnableThreadPool
@SpringCloudApplication
@MapperScan("com.atguigu.gmall.product.mapper")
public class ServiceProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceProductApplication.class,args);
    }
}
