package com.sishuok.spring4.genericinject;

import com.sishuok.spring4.genericinject.component.BeanInteface;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-31
 * <p>Version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GenericInjectTest.GenericInjectConfig.class)
public class GenericInjectTest {

    @Autowired
    private List<BeanInteface> beanIntefaceList;


    @Test
    public void test() {
        Assert.assertEquals(1, beanIntefaceList.size());
        //如果没有  public List<BeanInteface> customBeanIntefaceList() 则如下返回真
//        Assert.assertEquals(2, beanIntefaceList.size());
    }


    @Configuration
    @ComponentScan(basePackages = "com.sishuok.spring4.genericinject.component")
    static class GenericInjectConfig {

//        @Bean
//        public List<BeanInteface> customBeanIntefaceList() {
//            return Arrays.asList((BeanInteface)new BeanImpl1());
//        }

    }
}


