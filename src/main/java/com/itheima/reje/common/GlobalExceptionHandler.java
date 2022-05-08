package com.itheima.reje.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
// 拦截哪些controller进行异常处理，这里表明加了@RestController和@Controller注解的Controller会被拦截
@ResponseBody
// 返回数据是JSON格式
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 出现重复id的异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    // 处理sql的异常
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());

        // 若是异常信息中包含 Duplicate entry ，也就是针对这种异常该如何处理
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            // split[2] 创建的用户名在异常信息字符串的位置，具体查看异常信息的字符串
            return R.error(msg);
        }

        return R.error("未知错误");
    }


    // 抛出业务异常时的处理方法
    @ExceptionHandler(CustomException.class)
    public R<String> customExceptionHandler(CustomException cex) {
        log.error(cex.getMessage());

        return R.error(cex.getMessage());
    }
}
