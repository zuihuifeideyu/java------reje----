package com.itheima.reje;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.swing.*;

/**
 * @program: reje
 * @description:
 * @author: 作者名称
 * @date: 2022-05-03 14:32
 **/

@Slf4j
// 提供log方法
@SpringBootApplication
// SpringBoot的启动类
@ServletComponentScan
// 启动过滤器用于拦截资源
@EnableTransactionManagement
public class RejeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RejeApplication.class, args);
        log.info("项目启动成功...");
        // 输出日志
    }
}
