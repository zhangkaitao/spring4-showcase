package com.sishuok.spring.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-20
 * <p>Version: 1.0
 */
public class Test {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:spring-config.xml");
    }
}
