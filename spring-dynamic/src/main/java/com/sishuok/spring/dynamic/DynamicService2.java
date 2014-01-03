package com.sishuok.spring.dynamic;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-3
 * <p>Version: 1.0
 */
public class DynamicService2 {

    @Autowired
    private DynamicService1 dynamicService1;

    public String getMessage() {
        return dynamicService1.getMessage();
    }
}
