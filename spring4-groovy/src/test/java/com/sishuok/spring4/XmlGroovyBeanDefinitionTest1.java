package com.sishuok.spring4;

import com.sishuok.spring4.xml.MessagePrinter;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericGroovyApplicationContext;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-18
 * <p>Version: 1.0
 */
public class XmlGroovyBeanDefinitionTest1 {
    @Test
    public void test() {
        ApplicationContext ctx = new GenericGroovyApplicationContext("classpath:spring-config-xml.groovy");
        MessagePrinter messagePrinter = (MessagePrinter) ctx.getBean("messagePrinter");
        messagePrinter.printMessage();
        System.out.println(ctx.getBean("map"));
    }
}
