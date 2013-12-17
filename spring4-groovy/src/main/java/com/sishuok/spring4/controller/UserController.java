package com.sishuok.spring4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-13
 * <p>Version: 1.0
 */
@Controller
public class UserController {

    @RequestMapping("/hello")
    public String hello() {
        return "success";
    }
}
