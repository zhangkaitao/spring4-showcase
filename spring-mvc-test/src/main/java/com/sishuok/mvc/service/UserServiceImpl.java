package com.sishuok.mvc.service;

import com.sishuok.mvc.entity.User;
import org.springframework.stereotype.Service;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-29
 * <p>Version: 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    public User findById(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("zhang");
        return user;
    }
}
