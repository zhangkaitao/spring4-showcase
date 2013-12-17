package com.sishuok.spring4.validator;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-15
 * <p>Version: 1.0
 */
public class ForbiddenValidator implements ConstraintValidator<CheckPassword, String> {

    @Autowired
    private ApplicationContext ctx;

    private String[] forbiddenWords = {"admin"};

    @Override
    public void initialize(CheckPassword constraintAnnotation) {
        //初始化，得到注解数据
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(StringUtils.isEmpty(value)) {
            return true;
        }

        for(String word : forbiddenWords) {
            if(value.contains(word)) {
                ((ConstraintValidatorContextImpl)context).getConstraintDescriptor().getAttributes().put("word", word);
                return false;//验证失败
            }
        }
        return true;
    }
}
