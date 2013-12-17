package com.sishuok.spring4.service;

import com.sishuok.spring4.validator.CrossParameter;
import com.sishuok.spring4.validator.CrossParameterScriptAssert;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-15
 * <p>Version: 1.0
 */

@Validated
@Service
public class UserService {

    @CrossParameterScriptAssert(script = "args[0] == args[1]", lang = "javascript", alias = "args", message = "{password.confirmation.error}")
    public void changePassword(String password, String confirmation) {

    }
}
