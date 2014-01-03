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
class GroovyController {

    @RequestMapping("/groovy")
    public String hello() {
        return "hello";
    }

}
