package com.sishuok.spring4.generic;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-18
 * <p>Version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GenericConfig.class)
public class GenricInjectTest {
    @Autowired
    private Service<A, B> abService;

    @Autowired
    private Service<C, D> cdService;

    @Test
    public void test() {
        Assert.assertTrue(abService instanceof ABService);
        Assert.assertTrue(cdService instanceof CDService);
    }

    @Test
    public void testGenericAPI() {
        ResolvableType resolvableType = ResolvableType.forClass(ABService.class);
        System.out.println(resolvableType.getInterfaces()[0].getGeneric(1).resolve());
    }

}
