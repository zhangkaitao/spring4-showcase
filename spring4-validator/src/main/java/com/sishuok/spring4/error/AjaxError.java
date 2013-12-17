package com.sishuok.spring4.error;

import com.sishuok.spring4.SpringUtils;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.io.Serializable;
import java.util.*;

/**
 * field
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-16
 * <p>Version: 1.0
 */
public class AjaxError implements Serializable {

    private List<String> globalErrors;

    private List<Map<String, String>> fieldErrors;

    public List<String> getGlobalErrors() {
        return globalErrors;
    }

    public List<Map<String, String>> getFieldErrors() {
        return fieldErrors;
    }

    public static AjaxError from(Errors errors, Locale locale) {
        AjaxError ajaxError = new AjaxError();
        if(errors.hasGlobalErrors()) {
            ajaxError.globalErrors = new ArrayList<>();
            for(ObjectError error : errors.getGlobalErrors()) {
                ajaxError.globalErrors.add(getMessage(error, locale));
            }
        }

        if(errors.hasFieldErrors()) {
            ajaxError.fieldErrors = new ArrayList<>();
            for(FieldError error : errors.getFieldErrors()) {
                Map<String, String> errorData = new HashMap<>();
                errorData.put(error.getField(), getMessage(error, locale));
                ajaxError.fieldErrors.add(errorData);
            }
        }

        return ajaxError;
    }

    private static String getMessage(ObjectError error, Locale locale) {
        MessageSource messageSource = SpringUtils.getBean("messageSource");
        return messageSource.getMessage(error.getCode(), error.getArguments(), error.getDefaultMessage(), locale);
    }

}
