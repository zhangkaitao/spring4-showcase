package com.sishuok.mvc.client;

import com.sishuok.mvc.entity.User;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;

import java.util.Arrays;

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
public class EmbeddedJettyClientTest extends AbstractClientTest {

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
    public void testFindById() throws Exception {
        ResponseEntity<User> entity = restTemplate.getForEntity(baseUri + "/{id}", User.class, 1L);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertThat(entity.getHeaders().getContentType().toString(), containsString(MediaType.APPLICATION_JSON_VALUE));
        assertThat(entity.getBody(), hasProperty("name", is("zhang")));
    }


    @Test
    public void testSaveWithJson() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("zhang");

        String uri = baseUri;
        String createdLocation = baseUri + "/" + 1;

        restTemplate.setMessageConverters(Arrays.<HttpMessageConverter<?>>asList(new MappingJackson2HttpMessageConverter()));
        ResponseEntity<User> responseEntity = restTemplate.postForEntity(uri, user, User.class);

        assertEquals(createdLocation, responseEntity.getHeaders().get("Location").get(0));
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }


    @Test
    public void testSaveWithXML() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("zhang");

        String uri = baseUri;
        String createdLocation = baseUri + "/" + 1;

        restTemplate.setMessageConverters(Arrays.<HttpMessageConverter<?>>asList(new Jaxb2RootElementHttpMessageConverter()));
        ResponseEntity<User> responseEntity = restTemplate.postForEntity(uri, user, User.class);

        assertEquals(createdLocation, responseEntity.getHeaders().get("Location").get(0));
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    @Test
    public void testUpdate() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("zhang");

        String uri = baseUri + "/{id}";

        restTemplate.setMessageConverters(Arrays.<HttpMessageConverter<?>>asList(new MappingJackson2HttpMessageConverter()));
        ResponseEntity responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(user), (Class) null, user.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete() throws Exception {
        String uri = baseUri + "/{id}";
        Long id = 1L;

        ResponseEntity responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, HttpEntity.EMPTY, (Class) null, id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


}
