package com.sishuok.mvc.client;

import com.sishuok.mvc.entity.User;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
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
public class EmbeddedJettyClientTest {

    private static Server server;

    @BeforeClass
    public static void beforeClass() throws Exception {
        //创建一个server
        server = new Server(8080);
        WebAppContext context = new WebAppContext();
        String webapp = "spring-mvc-test/src/main/webapp";
        context.setDescriptor(webapp + "/WEB-INF/web.xml");  //指定web.xml配置文件
        context.setResourceBase(webapp);  //指定webapp目录
        context.setContextPath("/");
        context.setParentLoaderPriority(true);
        server.setHandler(context);
        server.start();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop(); //当测试结束时停止服务器
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

}
