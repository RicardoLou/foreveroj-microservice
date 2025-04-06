package com.Anglyao.foreverojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.Anglyao.foreverojbackenduserservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.Anglyao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.Anglyao.foreverojbackendserviceclient.service"})
public class ForeverojBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForeverojBackendUserServiceApplication.class, args);
    }

}
