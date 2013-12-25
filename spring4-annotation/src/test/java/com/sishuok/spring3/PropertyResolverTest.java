package com.sishuok.spring3;

import org.junit.Test;
import org.springframework.core.env.*;
import org.springframework.core.io.support.ResourcePropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-23
 * <p>Version: 1.0
 */
public class PropertyResolverTest {

    @Test
    public void test() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("encoding", "gbk");
        PropertySource propertySource1 = new MapPropertySource("map", map);

        ResourcePropertySource propertySource2 = new ResourcePropertySource(
                "resource", "classpath:resources.properties");


        MutablePropertySources propertySources = new MutablePropertySources();

        propertySources.addFirst(propertySource1);
        propertySources.addLast(propertySource2);

        PropertyResolver propertyResolver = new PropertySourcesPropertyResolver(propertySources);

        System.out.println(propertyResolver.getProperty("encoding"));
        System.out.println(propertyResolver.getProperty("no", "default"));
        System.out.println(propertyResolver.resolvePlaceholders("must be encoding ${encoding}"));
    }
}
