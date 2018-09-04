package com.longye.myproxy;
/**
 * Created by tianl on 2018/9/4.
 */
public class MyJdkProxyTest {

    public static void main(String[] args) {

        ChiHuo chiHuo = new ChiHuo();
        Person proxy = (Person) new Restaurant().getInstance(chiHuo);
        proxy.eat();
    }
}
