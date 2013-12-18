package com.sishuok.spring4.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-18
 * <p>Version: 1.0
 */
@RestController
class GroovyController2 {

    @RequestMapping("/groovy2")
    public String hello() {
        return "hello2";
    }

}
