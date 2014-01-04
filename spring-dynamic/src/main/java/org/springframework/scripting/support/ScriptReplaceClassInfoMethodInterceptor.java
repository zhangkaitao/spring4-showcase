package org.springframework.scripting.support;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-4
 * <p>Version: 1.0
 */
public class ScriptReplaceClassInfoMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        boolean isCglibMi = mi.getClass().getName().equals("org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation");
        if (isCglibMi && mi.getMethod().getDeclaringClass() != mi.getThis().getClass()) {
            MethodProxy methodProxy = (MethodProxy) ReflectionUtils.getField(methodProxyField, mi);
            Object fastClassInfo = ReflectionUtils.getField(fastClassInfoField, methodProxy);
            ReflectionUtils.setField(fastClassInfoF1Field, fastClassInfo, FastClass.create(mi.getThis().getClass()));
        }
        return mi.proceed();
    }

    private static Field methodProxyField;
    private static Field fastClassInfoField;
    private static Field fastClassInfoF1Field;

    static {
        try {
            methodProxyField = ReflectionUtils.findField(
                    Class.forName("org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation"),
                    "methodProxy");
            methodProxyField.setAccessible(true);

            fastClassInfoField = ReflectionUtils.findField(MethodProxy.class, "fastClassInfo");
            fastClassInfoField.setAccessible(true);

            Class<?> fastClassInfoClass = Class.forName("org.springframework.cglib.proxy.MethodProxy$FastClassInfo");
            fastClassInfoF1Field = ReflectionUtils.findField(fastClassInfoClass, "f1");
            fastClassInfoF1Field.setAccessible(true);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
