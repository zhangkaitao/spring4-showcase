package com.sishuok.spring4.annotation;

import com.sishuok.spring4.generic.A;
import com.sishuok.spring4.generic.ABService;
import com.sishuok.spring4.generic.B;
import org.hibernate.validator.constraints.Length;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-22
 * <p>Version: 1.0
 */
public class AnnotationUtilsTest {

    @Length.List(
            value = {
                    @Length(min = 1, max = 2, groups = A.class),
                    @Length(min = 3, max = 4, groups = B.class)
            }
    )
    @Scheduled
    @Test
    public void test() {
        Annotation service = AnnotationUtils.findAnnotation(ABService.class, org.springframework.stereotype.Service.class);
        Annotation component = AnnotationUtils.getAnnotation(service, org.springframework.stereotype.Component.class);
        System.out.println(component);

        Method method = ClassUtils.getMethod(AnnotationUtilsTest.class, "test");
        Set<Length> set = AnnotationUtils.getRepeatableAnnotation(method, Length.List.class, Length.class);
        System.out.println(set);
    }
}
