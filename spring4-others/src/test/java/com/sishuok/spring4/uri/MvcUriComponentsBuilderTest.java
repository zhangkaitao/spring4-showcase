package com.sishuok.spring4.uri;

import com.sishuok.spring4.controller.UserController;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-22
 * <p>Version: 1.0
 */
public class MvcUriComponentsBuilderTest {
    @Test
    public void test() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(req));

        //MvcUriComponentsBuilder类似于ServletUriComponentsBuilder，但是直接从控制器获取
        //类级别的
        System.out.println(
                fromController(UserController.class).build().toString()
        );

        //方法级别的
        System.out.println(
                fromMethodName(UserController.class, "view", 1L).build().toString()
        );

        //通过Mock方法调用得到
        System.out.println(
                fromMethodCall(on(UserController.class).getUser(2L)).build()
        );
    }
}
