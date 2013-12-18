package com.sishuok.spring4;

import com.sishuok.spring4.annotation.MessagePrinter;
import com.sishuok.spring4.xml.XmlBean;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-18
 * <p>Version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config-annotation.groovy", loader = GenericGroovyContextLoader.class)
public class AnnotationGroovyBeanDefinitionTest1 {

    @Autowired
    private MessagePrinter messagePrinter;

    @Autowired(required = false)
    private XmlBean xmlBean;


    @Test
    public void test() {
        messagePrinter.printMessage();
        Assert.assertNull(xmlBean);
    }
}
