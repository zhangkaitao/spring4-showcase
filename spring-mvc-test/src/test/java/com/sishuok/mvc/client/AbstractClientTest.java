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
    ObjectMapper objectMapper;
    Jaxb2Marshaller marshaller;
    String baseUri = "http://localhost:8080/users";

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();

        marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan(new String[]{"com.sishuok"});
        marshaller.afterPropertiesSet();

        restTemplate = new RestTemplate();
    }
}
