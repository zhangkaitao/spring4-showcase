package com.sishuok.spring4.socket;

import org.junit.Test;
import org.springframework.util.SocketUtils;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-22
 * <p>Version: 1.0
 */
public class SocketUtilsTest {

    @Test
    public void test() {
        System.out.println(SocketUtils.findAvailableUdpPort());
    }
}

