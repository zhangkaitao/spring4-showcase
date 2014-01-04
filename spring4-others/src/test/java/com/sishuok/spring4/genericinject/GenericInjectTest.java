package com.sishuok.spring4.genericinject;

import com.sishuok.spring4.genericinject.component.BeanImpl1;
import com.sishuok.spring4.genericinject.component.BeanImpl2;
import com.sishuok.spring4.genericinject.component.BeanInteface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-31
 * <p>Version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GenericInjectTest.GenericInjectConfig.class)
public class GenericInjectTest {

    @Autowired
    @Qualifier("myList1") //必须指定名字（因为xml中配置的集合没有泛型，除非只有一个集合 Bean）
    private List<BeanInteface> myList1;

    @Autowired
    @Qualifier("myList2") //必须指定名字（因为xml中配置的集合没有泛型，除非只有一个集合 Bean）
    private List<BeanInteface> myList2;

    @Autowired
    private List<BeanInteface> beanIntefaceList;


    @Test
    public void test() {
        assertEquals(1, beanIntefaceList.size());
        assertThat(beanIntefaceList, hasItem(isA(BeanImpl1.class)));

        //如果没有  public List<BeanInteface> customBeanIntefaceList() 则如下返回真
//        Assert.assertEquals(2, beanIntefaceList.size());

        assertEquals(1, myList1.size());
        assertThat(myList1, hasItem(isA(BeanImpl1.class)));

        assertEquals(1, myList2.size());
        assertThat(myList2, hasItem(isA(BeanImpl2.class)));

        RequestMappingHandlerMapping r;
    }


    @Configuration
    @ImportResource("classpath:spring-genericInject.xml")
    @ComponentScan(basePackages = "com.sishuok.spring4.genericinject.component")
    static class GenericInjectConfig {

        @Bean
        public List<BeanInteface> customBeanIntefaceList() {
            return Arrays.asList((BeanInteface) new BeanImpl1());
        }

        @Bean
        public List<BeanImpl1> customBeanBeanImpl1List() {
            return Arrays.asList(new BeanImpl1());
        }

    }
}


