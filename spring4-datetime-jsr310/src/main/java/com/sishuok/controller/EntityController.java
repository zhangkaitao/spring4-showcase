package com.sishuok.controller;

import com.sishuok.entity.Entity;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Created by Administrator on 13-12-20.
 */
@Controller
public class EntityController {

    @RequestMapping("/test")
    public String test(@ModelAttribute("entity") Entity entity) {
        RequestContextUtils.getTimeZone(request)
        System.out.println(LocaleContextHolder.getTimeZone());
        return "test";
    }
}
