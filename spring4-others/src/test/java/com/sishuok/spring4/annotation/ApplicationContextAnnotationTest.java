package com.sishuok.spring4.annotation;

import com.sishuok.spring4.generic.GenericConfig;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-22
 * <p>Version: 1.0
 */
public class ApplicationContextAnnotationTest {

    @Test
    public void test() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(GenericConfig.class);
        ctx.refresh();

        Map<String, Object> beans = ctx.getBeansWithAnnotation(org.springframework.stereotype.Service.class);
        System.out.println(beans);
    }
}
