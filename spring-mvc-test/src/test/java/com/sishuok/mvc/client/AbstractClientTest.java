package com.sishuok.mvc.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-29
 * <p>Version: 1.0
 */
public abstract class AbstractClientTest {

    static RestTemplate restTemplate;
    ObjectMapper objectMapper;//JSON
    Jaxb2Marshaller marshaller;//XML
    String baseUri = "http://localhost:8080/users";

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper(); //需要添加jackson jar包

        marshaller = new Jaxb2Marshaller(); //需要添加jaxb2实现（如xstream）
        marshaller.setPackagesToScan(new String[]{"com.sishuok"});
        marshaller.afterPropertiesSet();

        restTemplate = new RestTemplate();
    }
}
