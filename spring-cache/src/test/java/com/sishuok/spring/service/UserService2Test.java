package com.sishuok.spring.service;

import com.sishuok.spring.AppConfig;
import com.sishuok.spring.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-1
 * <p>Version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class UserService2Test {

    @Autowired
    private UserService2 userService;

    @Autowired
    private CacheManager cacheManager;

    private Cache userCache;

    @Before
    public void setUp() {
        userCache = cacheManager.getCache("user");
    }


    @Test
    public void testCache() {
        Long id = 1L;
        User user = new User(id, "zhang", "zhang@gmail.com");
        userService.save(user);

        //一定要复制一个 否则cache了(因为同一个JVM测试的)
        User user2 = new User(id, "zhang2", "zhang@gmail.com");
        userService.conditionUpdate(user2);

        userService.findById(id);
        userService.findByUsername("zhang2");

    }


}
