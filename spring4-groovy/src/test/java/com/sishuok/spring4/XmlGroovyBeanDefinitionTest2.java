package com.sishuok.spring4;

import com.sishuok.spring4.xml.MessagePrinter;
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
@ContextConfiguration(locations = "classpath:spring-config-xml.groovy", loader = GenericGroovyContextLoader.class)
public class XmlGroovyBeanDefinitionTest2 {

    @Autowired
    private MessagePrinter messagePrinter;

    @Test
    public void test() {
        messagePrinter.printMessage();
    }
}
