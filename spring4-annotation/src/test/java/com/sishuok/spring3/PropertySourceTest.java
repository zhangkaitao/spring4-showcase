package com.sishuok.spring3;

import org.junit.Test;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-23
 * <p>Version: 1.0
 */
public class PropertySourceTest {

    @Test
    public void test() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("encoding", "gbk");
        PropertySource propertySource1 = new MapPropertySource("map", map);
        System.out.println(propertySource1.getProperty("encoding"));

        ResourcePropertySource propertySource2 = new ResourcePropertySource("resource", "classpath:resources.properties");
        System.out.println(propertySource2.getProperty("encoding"));
    }

    @Test
    public void test2() throws IOException {

        Map<String, Object> map = new HashMap<>();
        map.put("encoding", "gbk");
        PropertySource propertySource1 = new MapPropertySource("map", map);

        ResourcePropertySource propertySource2 = new ResourcePropertySource("resource", "classpath:resources.properties");

        CompositePropertySource compositePropertySource = new CompositePropertySource("composite");
        compositePropertySource.addPropertySource(propertySource1);
        compositePropertySource.addPropertySource(propertySource2);
        System.out.println(compositePropertySource.getProperty("encoding"));
    }

    @Test
    public void test3() throws IOException {

        Map<String, Object> map = new HashMap<>();
        map.put("encoding", "gbk");
        PropertySource propertySource1 = new MapPropertySource("map", map);

        ResourcePropertySource propertySource2 = new ResourcePropertySource(
                "resource", "classpath:resources.properties");


        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addFirst(propertySource1);
        propertySources.addLast(propertySource2);
        System.out.println(propertySources.get("resource").getProperty("encoding"));

        for (PropertySource propertySource : propertySources) {
            System.out.println(propertySource.getProperty("encoding"));
        }


    }
}
