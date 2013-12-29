package com.sishuok.mvc.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sishuok.mvc.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-29
 * <p>Version: 1.0
 */
public class MockServerClientTest {

    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    @Before
    public void setUp() {
        //1.1、Rest客户端模板
        restTemplate = new RestTemplate();
        //1.2、模拟一个服务器
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }


    @Test
    public void test() throws JsonProcessingException {
        String baseUri = "http://localhost:8080/";
        String uri = baseUri + "user/{id}";
        Long id = 1L;
        User user = new User();
        user.setId(1L);
        user.setName("zhang");
        String requestUri = UriComponentsBuilder.fromUriString(uri).buildAndExpand(id).toUriString();

        //添加服务器端断言
        mockServer
                .expect(requestTo(requestUri))
                .andExpect(jsonPath("$.id").value(1))
                .andRespond(withSuccess(new ObjectMapper().writeValueAsString(user), MediaType.APPLICATION_JSON));

        //2、访问URI（与API交互）
        ResponseEntity<User> entity = restTemplate.getForEntity(uri, User.class, id);

        //3.1、客户端验证
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertThat(entity.getHeaders().getContentType().toString(), containsString(MediaType.APPLICATION_JSON_VALUE));
        assertThat(entity.getBody(), hasProperty("name", is("zhang")));

        //3.2、服务器端验证（验证之前添加的服务器端断言）
        mockServer.verify();
    }
}
