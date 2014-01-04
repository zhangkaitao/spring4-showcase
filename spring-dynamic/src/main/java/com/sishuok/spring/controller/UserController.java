package com.sishuok.spring.controller;

import com.sishuok.spring.dynamic.DynamicController;
import com.sishuok.spring.dynamic.DynamicDeployBeans;
import com.sishuok.spring.dynamic.DynamicService1;
import com.sishuok.spring.dynamic.DynamicService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-13
 * <p>Version: 1.0
 */

@Controller
public class UserController {

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private DynamicDeployBeans dynamicDeployBeans;

    @RequestMapping("/hello")
    public String hello() {
        DynamicService2 dynamicService2 = null;
        try {
            dynamicService2 = ctx.getBean(DynamicService2.class);
        } catch (Exception e) {
        }

        System.out.println(dynamicService2);
        if (dynamicService2 != null) {
            System.out.println("dynamicService2 : " + dynamicService2.getMessage());
        }
        return "success";
    }

    @RequestMapping("/registerBean")
    public String registerBean() {
        dynamicDeployBeans.registerBean(DynamicService1.class);
        dynamicDeployBeans.registerBean(DynamicService2.class);
        return "success";
    }

    @RequestMapping("/registerController")
    public String registerController() {
        dynamicDeployBeans.registerController(DynamicController.class);
        return "success";
    }

    @RequestMapping("/registerGroovyController")
    public String registerGroovyController() throws IOException {
        dynamicDeployBeans.registerGroovyController("classpath:com/sishuok/spring/dynamic/GroovyController.groovy");
        return "success";
    }

}
