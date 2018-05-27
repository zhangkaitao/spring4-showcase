package com.sishuok.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.servlet.http.HttpServletRequest;

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
    public String test(@ModelAttribute("entity") Entity entity, HttpServletRequest request) {
        RequestContextUtils.getTimeZone(request);
        System.out.println(LocaleContextHolder.getTimeZone());
        
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        entity.setDateTime(dateTime);
        entity.setDate(date);
        entity.setTime(time);
        
        return "test";
    }
}
