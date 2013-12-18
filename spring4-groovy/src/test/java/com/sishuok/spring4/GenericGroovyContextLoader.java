package com.sishuok.spring4;

import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.support.AbstractGenericContextLoader;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-18
 * <p>Version: 1.0
 */
public class GenericGroovyContextLoader extends AbstractGenericContextLoader {

    @Override
    protected String getResourceSuffix() {
        throw new UnsupportedOperationException(
                "GenericGroovyContextLoader does not support the getResourceSuffix() method");
    }

    @Override
    protected BeanDefinitionReader createBeanDefinitionReader(GenericApplicationContext context) {
        return new GroovyBeanDefinitionReader(context);
    }
}
