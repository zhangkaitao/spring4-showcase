package com.sishuok.spring4.controller;

import com.sishuok.spring4.entity.User;
import com.sishuok.spring4.error.AjaxError;
import com.sishuok.spring4.service.UserService;
import com.sishuok.spring4.validator.CrossParameter;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-13
 * <p>Version: 1.0
 */
@Controller
public class UserController {

    @RequestMapping("/save")
    public String save(@Valid User user, BindingResult result) {
        if(result.hasErrors()) {
            return "error";
        }
        return "success";
    }

    @RequestMapping("/ajax")
    @ResponseBody
    public Object ajaxError(@Valid User user, BindingResult result) {
        if(result.hasErrors()) {
            return AjaxError.from(result, null);
        }
        return "success";
    }


    @RequestMapping("/register")
    public String register(@Valid User user, BindingResult result) {
        if(result.hasErrors()) {
            return "error";
        }
        return "success";
    }


    @Autowired
    private UserService userService;


    @RequestMapping("/changePassword")
    public String changePassword(
            @RequestParam("password") String password,
            @RequestParam("confirmation") String confirmation, Model model) {
        try {
            userService.changePassword(password, confirmation);
        } catch (ConstraintViolationException e) {
            for(ConstraintViolation violation : e.getConstraintViolations()) {
                System.out.println(violation.getMessage());
            }
        }
        return "success";
    }

}
