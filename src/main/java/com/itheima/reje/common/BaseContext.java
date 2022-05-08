package com.itheima.reje.common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 */

// 作用范围为某个线程内，禁止跨线程操作
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}