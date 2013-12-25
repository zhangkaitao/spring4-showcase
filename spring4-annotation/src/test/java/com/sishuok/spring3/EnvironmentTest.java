package com.sishuok.spring3;

import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-23
 * <p>Version: 1.0
 */
public class EnvironmentTest {

    @Test
    public void test() {
        //会自动注册 System.getProperties() 和 System.getenv()
        Environment environment = new StandardEnvironment();
        System.out.println(environment.getProperty("file.encoding"));
        System.out.println(environment.getProperty("data"));


    }
}
