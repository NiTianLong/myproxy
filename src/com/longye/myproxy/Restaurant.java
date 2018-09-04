package com.longye.myproxy;

import java.lang.reflect.Method;

/**
 * 餐厅类
 * Created by tianl on 2018/9/4.
 */
public class Restaurant implements MyInvocationHandler {

    private Person target;

    public Object getInstance(Person target){
        this.target = target;
        Class clazz = target.getClass();
        return MyProxy.newProxyInstance(new MyClassLoader(), clazz.getInterfaces(), this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.err.println("厨师做好饭。");
        method.invoke(this.target, args);
        System.err.println("吃完结账。");
        return null;
    }
}
