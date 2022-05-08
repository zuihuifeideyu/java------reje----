package com.itheima.reje.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reje.common.BaseContext;
import com.itheima.reje.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: reje
 * @description: 检查用户是否完成
 * @author: 作者名称
 * @date: 2022-05-04 15:10
 **/

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
// 表示对所有请求都进行拦截处理
@Slf4j
public class LoginCheckFilter implements Filter {

    // 专门用来进行路径比较的类,路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        log.info("拦截到请求：{}", httpServletRequest.getRequestURI());

        // 获取这次请求的URI，判断本次请求是否需要进行拦截处理
        String requestURI = httpServletRequest.getRequestURI();
        String[] urls = new String[] {"/employee/login", "/employee/logout", "backend/**", "front/**", "/user/sendMsg",
                "/user/login"};
        // 定义不需要处理的路径

        //进行路径匹配
        boolean check = Check(urls, requestURI);

        if(check) {
            // 判断是不是要拦截的资源
            log.info("本次请求不需要处理{}", requestURI);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        } else {
            // 判断是否登陆
            if (httpServletRequest.getSession().getAttribute("employee") != null) {

                log.info("用户已登录，用户id为：{}",httpServletRequest.getSession().getAttribute("employee"));

                Long empId = (Long) httpServletRequest.getSession().getAttribute("employee"); // 获取当前用户的id
                BaseContext.setCurrentId(empId); // 将当前用户的id设置为该线程的id

                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
        }



        //4-2、判断登录状态，如果已登录，则直接放行
        if(httpServletRequest.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",httpServletRequest.getSession().getAttribute("user"));

            Long userId = (Long) httpServletRequest.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }


        // 被拦截的资源用输出流数据响应前端
        httpServletResponse.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;


    }


    // 进行路径匹配
    public boolean Check(String[] urls, String requestURI) {
        for (String url: urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);

            if(match) {
                return true;
            }
        }

        return false;
    }
}
