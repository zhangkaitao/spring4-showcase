package com.sishuok.spring4.controller;

import com.sishuok.spring4.generic.A;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-22
 * <p>Version: 1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/{id}")
    public String view(@PathVariable("id") Long id) {
        return "view";
    }

    @RequestMapping("/{id}")
    public A getUser(@PathVariable("id") Long id) {
        return new A();
    }

}
