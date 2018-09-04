package com.longye.myproxy;

import java.lang.reflect.Method;

/**
 * Created by tianl on 2018/9/4.
 */
public interface MyInvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
