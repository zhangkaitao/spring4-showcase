package com.sishuok.spring.dynamic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-3
 * <p>Version: 1.0
 */
@Controller
public class DynamicController {

    @RequestMapping("/controller")
    public String controller() {
        return "success";
    }
}
