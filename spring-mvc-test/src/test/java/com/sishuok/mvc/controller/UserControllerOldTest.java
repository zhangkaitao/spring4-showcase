package com.sishuok.mvc.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;


/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-26
 * <p>Version: 1.0
 */
public class UserControllerOldTest {

    private UserController userController;

    @Before
    public void setUp() {
        userController = new UserController();
        //安装userCtroller依赖 比如userService
    }

    @Test
    public void testView() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        ModelAndView mv = userController.view(1L, req);

        ModelAndViewAssert.assertViewName(mv, "user/view");
        ModelAndViewAssert.assertModelAttributeAvailable(mv, "user");

    }

}
