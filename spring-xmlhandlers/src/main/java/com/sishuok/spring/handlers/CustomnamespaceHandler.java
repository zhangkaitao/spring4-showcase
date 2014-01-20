package com.sishuok.spring.handlers;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-20
 * <p>Version: 1.0
 */
public class CustomnamespaceHandler extends NamespaceHandlerSupport {


    @Override
    public void init() {
        registerBeanDefinitionParser("hello", new HelloBeanDefinitionParser());
    }
}
