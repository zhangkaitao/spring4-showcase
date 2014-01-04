package com.sishuok.spring.service;

import com.sishuok.spring.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-1
 * <p>Version: 1.0
 */
@Service
public class UserService {

    Set<User> users = new HashSet<User>();

    @CachePut(value = "user", key = "#user.id")
    public User save(User user) {
        users.add(user);
        return user;
    }

    @CachePut(value = "user", key = "#user.id")
    public User update(User user) {
        users.remove(user);
        users.add(user);
        return user;
    }

    @CacheEvict(value = "user", key = "#user.id")
    public User delete(User user) {
        users.remove(user);
        return user;
    }

    @CacheEvict(value = "user", allEntries = true)
    public void deleteAll() {
        users.clear();
    }

    @Cacheable(value = "user", key = "#id")
    public User findById(final Long id) {
        System.out.println("cache miss, invoke find by id, id:" + id);
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

}
