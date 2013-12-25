package com.sishuok.spring4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-13
 * <p>Version: 1.0
 */

@Controller
public class UserController {

    @Autowired
    Environment env;

    @Autowired
    private ApplicationContext ctx;

    @RequestMapping("/hello")
    public String hello() {
        System.out.println(env.getProperty("myConfig"));
        System.out.println(Arrays.toString(env.getActiveProfiles()));
        System.out.println(env.getProperty("contextConfigLocation"));
        return "success";
    }

}
