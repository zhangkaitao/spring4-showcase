package com.sishuok.spring.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sishuok.spring.AppConfig;
import com.sishuok.spring.entity.User;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-1
 * <p>Version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

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
        assertNotNull(userCache.get(id).get());
        userService.delete(user);
        assertNull(userCache.get(id));
        
        userService.save(user);
        assertNotNull(userCache.get(id).get());
        userService.deleteAll();
        assertNull(userCache.get(id));
    }
}
