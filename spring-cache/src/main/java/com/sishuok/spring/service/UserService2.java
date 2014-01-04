package com.sishuok.spring.service;

import com.sishuok.spring.entity.User;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-1
 * <p>Version: 1.0
 */
@Service
public class UserService2 {

    Set<User> users = new HashSet<User>();

    @Caching(
            put = {
                    @CachePut(value = "user", key = "#user.id"),
                    @CachePut(value = "user", key = "#user.username"),
                    @CachePut(value = "user", key = "#user.email")
            }
    )
    public User save(User user) {
        users.add(user);
        return user;
    }

    @Caching(
            put = {
                    @CachePut(value = "user", key = "#user.id"),
                    @CachePut(value = "user", key = "#user.username"),
                    @CachePut(value = "user", key = "#user.email")
            }
    )
    public User update(User user) {
        users.remove(user);
        users.add(user);
        return user;
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "user", key = "#user.id"),
                    @CacheEvict(value = "user", key = "#user.username"),
                    @CacheEvict(value = "user", key = "#user.email")
            }
    )
    public User delete(User user) {
        users.remove(user);
        return user;
    }

    @CacheEvict(value = "user", allEntries = true)
    public void deleteAll() {
        users.clear();
    }

    @Caching(
            cacheable = {
                    @Cacheable(value = "user", key = "#id")
            },
            put = {
                    @CachePut(value = "user", key = "#result.username", condition = "#result != null"),
                    @CachePut(value = "user", key = "#result.email", condition = "#result != null")
            }
    )
    public User findById(final Long id) {
        System.out.println("cache miss, invoke find by id, id:" + id);
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }


    @Caching(
            cacheable = {
                    @Cacheable(value = "user", key = "#username")
            },
            put = {
                    @CachePut(value = "user", key = "#result.id", condition = "#result != null"),
                    @CachePut(value = "user", key = "#result.email", condition = "#result != null")
            }
    )
    public User findByUsername(final String username) {
        System.out.println("cache miss, invoke find by username, username:" + username);
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Caching(
            cacheable = {
                    @Cacheable(value = "user", key = "#email")
            },
            put = {
                    @CachePut(value = "user", key = "#result.id", condition = "#result != null"),
                    @CachePut(value = "user", key = "#result.email", condition = "#result != null")
            }
    )
    public User findByEmail(final String email) {
        System.out.println("cache miss, invoke find by email, email:" + email);
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }


    @Caching(
            evict = {
//                    @CacheEvict(value = "user", key = "#user.id", condition = "#root.target.canCache() and #root.caches[0].get(#user.id).get().username ne #user.username", beforeInvocation = true)
                    @CacheEvict(value = "user", key = "#user.id", condition = "#root.target.canEvict(#root.caches[0], #user.id, #user.username)", beforeInvocation = true)
            }
    )
    public void conditionUpdate(User user) {
        users.remove(user);
        users.add(user);
    }


    public boolean canEvict(Cache userCache, Long id, String username) {
        User cacheUser = userCache.get(id, User.class);
        if (cacheUser == null) {
            return false;
        }
        return !cacheUser.getUsername().equals(username);
    }

}
