package com.sishuok.mvc.controller;

import com.sishuok.mvc.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-26
 * <p>Version: 1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/{id}")
    public ModelAndView view(@PathVariable("id") Long id, HttpServletRequest req) {
        User user = new User();
        user.setId(id);
        user.setName("zhang");

        ModelAndView mv = new ModelAndView();
        mv.addObject("user", user);
        mv.setViewName("user/view");
        return mv;
    }


    @RequestMapping(method = RequestMethod.POST)
    public String create(
            @ModelAttribute("user") User user,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if ("admin".equals(user.getName())) {
            result.rejectValue("name", "用户名不能为admin");
            return "showCreateForm";
        } else {
            redirectAttributes.addFlashAttribute("success", "创建成功");
        }
        return "redirect:/user";
    }

    @RequestMapping(value = "/{id}/icon", method = RequestMethod.POST)
    public String uploadIcon(
            @PathVariable("id") Long id,
            @RequestParam("icon") MultipartFile icon, Model model) throws IOException {

        model.addAttribute("icon", icon.getBytes());

        return "success";
    }


    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public User json(@RequestBody User user) {
        return user;
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public User xml(@RequestBody User user) {
        return user;
    }


    @RequestMapping("/exception")
    public String exception() {
        throw new IllegalArgumentException();
    }


    @RequestMapping(value = "/async1", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Callable<User> async1(final User user) {
        return new Callable<User>() {
            @Override
            public User call() throws Exception {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                }
                return user;
            }
        };
    }

    @RequestMapping(value = "/async2", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DeferredResult<User> async2(final User user) {
        final DeferredResult<User> result = new DeferredResult<User>();
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                }
                result.setResult(user);
            }
        }.start();
        return result;
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(IllegalArgumentException e) {
        return "error";
    }

}
