package com.longye.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 餐厅类
 * Created by tianl on 2018/9/4.
 */
public class Restaurant implements InvocationHandler {

    private Person target;

    public Object getInstance(Person target){
        this.target = target;
        Class clazz = target.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.err.println("厨师做好饭。");
        method.invoke(this.target, args);
        System.err.println("吃完结账。");
        return null;
    }
}
