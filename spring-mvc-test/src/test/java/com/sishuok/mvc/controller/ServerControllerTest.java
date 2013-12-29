package com.sishuok.mvc.controller;

import com.sishuok.matcher.HasProperty;
import com.sishuok.mvc.entity.User;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-28
 * <p>Version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "spring-mvc-test/src/main/webapp")
@ContextHierarchy({
        @ContextConfiguration(name = "parent", locations = "classpath:spring-config.xml"),
        @ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml")
})
public class ServerControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(wac).build();
    }


    @Test
    public void test1() throws Exception {

        //测试普通控制器
        mockMvc.perform(get("/user/{id}", 1)) //执行请求
                .andExpect(model().attributeExists("user")) //验证存储模型数据
                .andExpect(model().attribute("user", hasProperty("name", equalTo("zhang")))) //验证存储模型数据
                .andExpect(view().name("user/view")) //验证viewName
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/view.jsp"))//验证视图渲染时forward到的jsp
                .andExpect(status().isOk())//验证状态码
                .andDo(print()); //输出MvcResult到控制台
    }


    @Test
    public void test2() throws Exception {
        //找不到控制器，404测试
        MvcResult result = mockMvc.perform(get("/user2/{id}", 1)) //执行请求
                .andDo(print())
                .andExpect(status().isNotFound()) //验证控制器不存在
                .andReturn();
        Assert.assertNull(result.getModelAndView()); //自定义断言
    }

    @Test
    public void test3() throws Exception {
        //得到MvcResult自己验证
        MvcResult result = mockMvc.perform(get("/user/{id}", 1))//执行请求
                .andReturn(); //返回MvcResult
        Assert.assertNotNull(result.getModelAndView().getModel().get("user")); //自定义断言
    }


    @Test
    public void test4() throws Exception {
        //验证请求参数绑定
        mockMvc.perform(post("/user").param("name", "zhang")) //执行传递参数的POST请求(也可以post("/user?name=zhang"))
                .andExpect(handler().handlerType(UserController.class)) //验证执行的控制器类型
                .andExpect(handler().methodName("create")) //验证执行的控制器方法名
                .andExpect(model().hasNoErrors()) //验证页面没有错误
                .andExpect(flash().attributeExists("success")) //验证存在flash属性
                .andExpect(view().name("redirect:/user")); //验证视图
    }

    @Test
    public void test5() throws Exception {
        //验证请求参数验证失败
        mockMvc.perform(post("/user").param("name", "admin")) //执行请求
                .andExpect(model().hasErrors()) //验证模型有错误
                .andExpect(model().attributeDoesNotExist("name")) //验证存在错误的属性
                .andExpect(view().name("showCreateForm")); //验证视图
    }


    @Test
    public void test6() throws Exception {
        //文件上传
        byte[] bytes = new byte[]{1, 2};
        mockMvc.perform(fileUpload("/user/{id}/icon", 1L).file("icon", bytes)) //执行文件上传
                .andExpect(model().attribute("icon", bytes)) //验证属性相等性
                .andExpect(view().name("success")); //验证视图
    }

    @Test
    public void test7() throws Exception {
        //JSON请求/响应
        String requestBody = "{\"id\":1, \"name\":\"zhang\"}";
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody)
                .accept(MediaType.APPLICATION_JSON)) //执行请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //验证响应contentType
                .andExpect(jsonPath("$.id").value(1)); //使用Json path验证JSON 请参考http://goessner.net/articles/JsonPath/

        String errorBody = "{id:1, name:zhang}";
        MvcResult result = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON).content(errorBody)
                .accept(MediaType.APPLICATION_JSON)) //执行请求
                .andExpect(status().isBadRequest()) //400错误请求
                .andReturn();

        Assert.assertTrue(HttpMessageNotReadableException.class.isAssignableFrom(result.getResolvedException().getClass()));  //错误的请求内容体
    }

    @Test
    public void test8() throws Exception {
        //XML请求/响应
        String requestBody = "<user><id>1</id><name>zhang</name></user>";
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_XML).content(requestBody)
                .accept(MediaType.APPLICATION_XML)) //执行请求
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_XML)) //验证响应contentType
                .andExpect(xpath("/user/id/text()").string("1")); //使用XPath表达式验证XML 请参考http://www.w3school.com.cn/xpath/

        String errorBody = "<user><id>1</id><name>zhang</name>";
        MvcResult result = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_XML).content(errorBody)
                .accept(MediaType.APPLICATION_XML)) //执行请求
                .andExpect(status().isBadRequest()) //400错误请求
                .andReturn();

        Assert.assertTrue(HttpMessageNotReadableException.class.isAssignableFrom(result.getResolvedException().getClass()));//错误的请求内容体
    }

    @Test
    public void test9() throws Exception {
        //异常处理
        MvcResult result = mockMvc.perform(get("/user/exception")) //执行请求
                .andExpect(status().isInternalServerError()) //验证服务器内部错误
                .andReturn();

        Assert.assertTrue(IllegalArgumentException.class.isAssignableFrom(result.getResolvedException().getClass()));
    }


    @Test
    public void test10() throws Exception {
        //静态资源
        mockMvc.perform(get("/static/app.js")) //执行请求
                .andExpect(status().isOk()) //验证状态码200
                .andExpect(content().string(CoreMatchers.containsString("var")));//验证渲染后的视图内容包含var


        mockMvc.perform(get("/static/app1.js")) //执行请求
                .andExpect(status().isNotFound());  //验证状态码404
    }

    @Test
    public void test11() throws Exception {
        //异步测试

        //Callable
        MvcResult result = mockMvc.perform(get("/user/async1?id=1&name=zhang")) //执行请求
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(CoreMatchers.instanceOf(User.class))) //默认会等10秒超时
                .andReturn();


        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        //DeferredResult
        result = mockMvc.perform(get("/user/async2?id=1&name=zhang")) //执行请求
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(CoreMatchers.instanceOf(User.class)))  //默认会等10秒超时
                .andReturn();


        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }


    @Test
    public void test12() throws Exception {
        //Filter
        mockMvc = webAppContextSetup(wac).addFilter(new MyFilter(), "/*").build();
        mockMvc.perform(get("/user/1"))
                .andExpect(request().attribute("filter", true));

    }

    @Test
    public void test13() throws Exception {
        //全局配置
        mockMvc = webAppContextSetup(wac)
                .defaultRequest(get("/user/1").requestAttr("default", true)) //默认请求 如果其是Mergeable类型的，会自动合并的哦mockMvc.perform中的RequestBuilder
                .alwaysDo(print())  //默认每次执行请求后都做的动作
                .alwaysExpect(request().attribute("default", true)) //默认每次执行后进行验证的断言
                .build();

        mockMvc.perform(get("/user/1"))
                .andExpect(model().attributeExists("user"));

    }

    private static class MyFilter implements Filter {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            request.setAttribute("filter", true);
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {
        }
    }


    @Test
    public void testNestedProperty() {
        List<User> userlist = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setName("zhang");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("admin");
//        user2.setUser2(user2);

        userlist.add(user1);
        userlist.add(user2);

        assertThat((List<Object>) (List) userlist, hasItem(
//                HasPropertyWithValue.hasProperty("user2.name", is("admin"))
                HasProperty.hasProperty("user2.name")
        ));

    }

}
