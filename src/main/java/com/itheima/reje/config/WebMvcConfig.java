package com.itheima.reje.config;

import com.itheima.reje.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @program: reje
 * @description:
 * @author: 作者名称
 * @date: 2022-05-03 14:55
 **/


// 用来对静态资源进行映射，直接放在backend和front中无法访问到，
// 默认为访问src/main/resources/static 或者 templates下的文件(静态资源或html、jsp)

@Configuration
@Slf4j
public class WebMvcConfig extends WebMvcConfigurationSupport {


    // 设置静态资源映射
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {

        log.info("开始进行静态资源映射...");

        registry.addResourceHandler("backend/**").addResourceLocations("classpath:/backend/");
        // addResourceHandler表示前端要访问的资源是以什么样的形式访问，addResourceLocations表示从哪里找资源去回馈给前端

        registry.addResourceHandler("front/**").addResourceLocations("classpath:/front/");

    }


    // 扩展mvc框架的消息转换器
    // 重写数据类型转换的方法
    // 一般消息转换器将后端要返回给前端的java对象转换为json的格式，方便前端读取利用
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        // 创建消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置对象转换器
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0,messageConverter);
        // 0 代表优先级，设置我们自己的转换器到最高优先级
    }
}
