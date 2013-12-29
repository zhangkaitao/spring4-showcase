package com.sishuok.mvc.client;

import com.sishuok.mvc.controller.UserRestController;
import com.sishuok.mvc.entity.User;
import com.sishuok.mvc.service.UserService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-29
 * <p>Version: 1.0
 */
public class SpringBootClientTest {

    private static ApplicationContext ctx;

    @BeforeClass
    public static void beforeClass() throws Exception {
        ctx = SpringApplication.run(Config.class); //启动服务器 加载Config指定的组件
    }

    @AfterClass
    public static void afterClass() throws Exception {
        SpringApplication.exit(ctx);//退出服务器
    }

    @Test
    public void test() throws Exception {

        RestTemplate template = new RestTemplate();
        String baseUri = "http://localhost:8080/";
        ResponseEntity<User> entity = template.getForEntity(baseUri + "user/{id}", User.class, 1L);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertThat(entity.getHeaders().getContentType().toString(), containsString(MediaType.APPLICATION_JSON_VALUE));
        assertThat(entity.getBody(), hasProperty("name", is("zhang")));

    }

    @Configuration
    @EnableAutoConfiguration
    static class Config {
        @Bean
        public UserRestController userController() {
            return new UserRestController(userService());
        }

        @Bean
        public UserService userService() {
            //Mockito请参考 http://stamen.iteye.com/blog/1470066
            UserService userService = Mockito.mock(UserService.class);
            User user = new User();
            user.setId(1L);
            user.setName("zhang");
            Mockito.when(userService.findById(Mockito.any(Long.class))).thenReturn(user);
            return userService;

//            return new UserServiceImpl(); //此处也可以返回真实的UserService实现
        }
    }

}
