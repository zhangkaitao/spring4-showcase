package com.sishuok.mvc.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-26
 * <p>Version: 1.0
 */

//XML风格
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextHierarchy({
        @ContextConfiguration(name = "parent", locations = "classpath:spring-config.xml"),
        @ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml")
})

//注解风格
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration(value = "src/main/webapp")
//@ContextHierarchy({
//        @ContextConfiguration(name = "parent", classes = AppConfig.class),
//        @ContextConfiguration(name = "child", classes = MvcConfig.class)
//})
public class UserControllerWebAppContextSetupTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testView() throws Exception {
        mockMvc
                .perform(get("/user/1"))
                .andExpect(view().name("user/view"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/view.jsp"))
                .andExpect(model().attributeExists("user"))
                .andExpect(content().string(Matchers.contains("你好")))
                .andExpect(status().isOk())
                .andDo(print());
    }


}
