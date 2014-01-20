package com.sishuok.spring.handlers;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-20
 * <p>Version: 1.0
 */
public class HelloBeanDefinitionParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        System.out.println("---parse hello namespace");
        return null;
    }
}
