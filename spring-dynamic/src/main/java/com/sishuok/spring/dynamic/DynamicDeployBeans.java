package com.sishuok.spring.dynamic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.scripting.groovy.GroovyScriptFactory;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.scripting.support.ScriptFactoryPostProcessor;
import org.springframework.util.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethodSelector;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-3
 * <p>Version: 1.0
 */
public class DynamicDeployBeans {

    protected static final Log logger = LogFactory.getLog(DynamicDeployBeans.class);

    //DefaultAnnotationHandlerMapping
    private static Method determineUrlsForHandlerMethod =
            ReflectionUtils.findMethod(DefaultAnnotationHandlerMapping.class, "determineUrlsForHandler", String.class);
    private static Method registerHandlerMethod =
            ReflectionUtils.findMethod(DefaultAnnotationHandlerMapping.class, "registerHandler", String[].class, String.class);
    private static Field handlerMapField =
            ReflectionUtils.findField(DefaultAnnotationHandlerMapping.class, "handlerMap", Map.class);

    //RequestMappingHandlerMapping
    private static Method detectHandlerMethodsMethod =
            ReflectionUtils.findMethod(RequestMappingHandlerMapping.class, "detectHandlerMethods", Object.class);
    private static Method getMappingForMethodMethod =
            ReflectionUtils.findMethod(RequestMappingHandlerMapping.class, "getMappingForMethod", Method.class, Class.class);
    private static Method getMappingPathPatternsMethod =
            ReflectionUtils.findMethod(RequestMappingHandlerMapping.class, "getMappingPathPatterns", RequestMappingInfo.class);
    private static Method getPathMatcherMethod =
            ReflectionUtils.findMethod(RequestMappingHandlerMapping.class, "getPathMatcher");
    private static Field handlerMethodsField =
            ReflectionUtils.findField(RequestMappingHandlerMapping.class, "handlerMethods", Map.class);
    private static Field urlMapField =
            ReflectionUtils.findField(RequestMappingHandlerMapping.class, "urlMap", MultiValueMap.class);

    private static final String SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME =
            "org.springframework.scripting.config.scriptFactoryPostProcessor";

    static {
        detectHandlerMethodsMethod.setAccessible(true);
        registerHandlerMethod.setAccessible(true);
        handlerMapField.setAccessible(true);

        detectHandlerMethodsMethod.setAccessible(true);
        getMappingForMethodMethod.setAccessible(true);
        getMappingPathPatternsMethod.setAccessible(true);
        getPathMatcherMethod.setAccessible(true);
        handlerMethodsField.setAccessible(true);
        urlMapField.setAccessible(true);
    }

    private ApplicationContext ctx;
    private DefaultListableBeanFactory beanFactory;

    private boolean hasRegisterScriptFactoryPostProcessor = false;

    private Map<String, Long> scriptLastModifiedMap = new ConcurrentHashMap<>();//in millis

    public DynamicDeployBeans() {
        this(-1L);
    }

    public DynamicDeployBeans(Long scriptCheckInterval) {
        if (scriptCheckInterval > 0L) {
            startScriptModifiedCheckThead(scriptCheckInterval);
        }
    }


    @Autowired
    public void setApplicationContext(ApplicationContext ctx) {
        if (!DefaultListableBeanFactory.class.isAssignableFrom(ctx.getAutowireCapableBeanFactory().getClass())) {
            throw new IllegalArgumentException("BeanFactory must be DefaultListableBeanFactory type");
        }
        this.ctx = ctx;
        this.beanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
    }

    public void registerBean(Class<?> beanClass) {
        registerBean(null, beanClass);
    }

    public void registerBean(String beanName, Class<?> beanClass) {
        Assert.notNull(beanClass, "register bean class must not null");
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(beanClass);

        if (StringUtils.hasText(beanName)) {
            beanFactory.registerBeanDefinition(beanName, bd);
        } else {
            BeanDefinitionReaderUtils.registerWithGeneratedName(bd, beanFactory);
        }

    }

    public void registerController(Class<?> controllerClass) {
        Assert.notNull(controllerClass, "register controller bean class must not null");
        if (!WebApplicationContext.class.isAssignableFrom(ctx.getClass())) {
            throw new IllegalArgumentException("applicationContext must be WebApplicationContext type");
        }

        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(controllerClass);

        String controllerBeanName = controllerClass.getName();
        removeOldControllerMapping(controllerBeanName);
        beanFactory.registerBeanDefinition(controllerBeanName, bd);
        addControllerMapping(controllerBeanName);
    }


    public void registerGroovyController(String scriptLocation) throws IOException {

        if (scriptNotExists(scriptLocation)) {
            throw new IllegalArgumentException("script not exists : " + scriptLocation);
        }
        scriptLastModifiedMap.put(scriptLocation, scriptLastModified(scriptLocation));

        //registerScriptFactoryPostProcessorIfNecessary();

        // Create script factory bean definition.

        GroovyScriptFactory groovyScriptFactory = new GroovyScriptFactory(scriptLocation);
        Object controller =
                groovyScriptFactory.getScriptedObject(new ResourceScriptSource(ctx.getResource(scriptLocation)));

        String controllerBeanName = scriptLocation;
        removeOldControllerMapping(controllerBeanName);
        beanFactory.destroySingleton(controllerBeanName);
        beanFactory.registerSingleton(controllerBeanName, controller);
        addControllerMapping(controllerBeanName);
    }


    private void removeOldControllerMapping(String controllerBeanName) {

        if (!beanFactory.containsBean(controllerBeanName)) {
            return;
        }
        DefaultAnnotationHandlerMapping annotationHandlerMapping = null;
        RequestMappingHandlerMapping requestMappingHandlerMapping = null;

        if ((annotationHandlerMapping = defaultAnnotationHandlerMapping()) != null) {
            //spring 3.1 之前
            String[] urls = (String[]) ReflectionUtils.invokeMethod(determineUrlsForHandlerMethod, annotationHandlerMapping, controllerBeanName);
            if (!StringUtils.isEmpty(urls)) {
                Map handlerMap =
                        (Map) ReflectionUtils.getField(handlerMapField, annotationHandlerMapping);
                //remove old
                for (String url : urls) {
                    handlerMap.remove(url);
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Rejected bean name '" + controllerBeanName + "': no URL paths identified");
                }
            }

        } else if ((requestMappingHandlerMapping = requestMappingHandlerMapping()) != null) {
            //remove old
            Class<?> handlerType = ctx.getType(controllerBeanName);
            final Class<?> userType = ClassUtils.getUserClass(handlerType);

            Map handlerMethods = (Map) ReflectionUtils.getField(handlerMethodsField, requestMappingHandlerMapping);
            MultiValueMap urlMapping = (MultiValueMap) ReflectionUtils.getField(urlMapField, requestMappingHandlerMapping);

            final RequestMappingHandlerMapping innerRequestMappingHandlerMapping = requestMappingHandlerMapping;
            Set<Method> methods = HandlerMethodSelector.selectMethods(userType, new ReflectionUtils.MethodFilter() {
                @Override
                public boolean matches(Method method) {
                    return ReflectionUtils.invokeMethod(
                            getMappingForMethodMethod,
                            innerRequestMappingHandlerMapping,
                            method, userType) != null;
                }
            });

            for (Method method : methods) {
                RequestMappingInfo mapping =
                        (RequestMappingInfo) ReflectionUtils.invokeMethod(getMappingForMethodMethod, requestMappingHandlerMapping, method, userType);

                handlerMethods.remove(mapping);

                Set<String> patterns =
                        (Set<String>) ReflectionUtils.invokeMethod(getMappingPathPatternsMethod, requestMappingHandlerMapping, mapping);

                PathMatcher pathMatcher =
                        (PathMatcher) ReflectionUtils.invokeMethod(getPathMatcherMethod, requestMappingHandlerMapping);

                for (String pattern : patterns) {
                    if (!pathMatcher.isPattern(pattern)) {
                        urlMapping.remove(pattern);
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("applicationContext must contains DefaultAnnotationHandlerMapping or RequestMappingHandlerMapping bean");
        }

    }


    private void addControllerMapping(String controllerBeanName) {

        removeOldControllerMapping(controllerBeanName);

        DefaultAnnotationHandlerMapping annotationHandlerMapping = null;
        RequestMappingHandlerMapping requestMappingHandlerMapping = null;

        if ((annotationHandlerMapping = defaultAnnotationHandlerMapping()) != null) {
            //spring 3.1 之前
            String[] urls = (String[]) ReflectionUtils.invokeMethod(determineUrlsForHandlerMethod, annotationHandlerMapping, controllerBeanName);
            if (!StringUtils.isEmpty(urls)) {
                // URL paths found: Let's consider it a handler.
                ReflectionUtils.invokeMethod(registerHandlerMethod, annotationHandlerMapping, urls, controllerBeanName);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Rejected bean name '" + controllerBeanName + "': no URL paths identified");
                }
            }

        } else if ((requestMappingHandlerMapping = requestMappingHandlerMapping()) != null) {
            //spring 3.1 开始
            ReflectionUtils.invokeMethod(detectHandlerMethodsMethod, requestMappingHandlerMapping, controllerBeanName);
        } else {
            throw new IllegalArgumentException("applicationContext must contains DefaultAnnotationHandlerMapping or RequestMappingHandlerMapping bean");
        }


    }

    private DefaultAnnotationHandlerMapping defaultAnnotationHandlerMapping() {
        try {
            return ctx.getBean(DefaultAnnotationHandlerMapping.class);
        } catch (Exception e) {
            return null;
        }
    }

    private RequestMappingHandlerMapping requestMappingHandlerMapping() {
        try {
            return ctx.getBean(RequestMappingHandlerMapping.class);
        } catch (Exception e) {
            return null;
        }
    }


    private void registerScriptFactoryPostProcessorIfNecessary() {
        if (!hasRegisterScriptFactoryPostProcessor) {
            hasRegisterScriptFactoryPostProcessor = ctx.containsBeanDefinition(SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME);
            if (!hasRegisterScriptFactoryPostProcessor) {
                BeanDefinition beanDefinition = new RootBeanDefinition(ScriptFactoryPostProcessor.class);
                beanFactory.registerBeanDefinition(SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME, beanDefinition);
                beanFactory.addBeanPostProcessor(ctx.getBean(ScriptFactoryPostProcessor.class));

            }
        }
    }


    private void startScriptModifiedCheckThead(final Long scriptCheckInterval) {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {

                        Thread.sleep(scriptCheckInterval);

                        Map<String, Long> copyMap = new HashMap<>(scriptLastModifiedMap);
                        for (String scriptLocation : copyMap.keySet()) {

                            if (scriptNotExists(scriptLocation)) {
                                scriptLastModifiedMap.remove(scriptLocation);
                                //TODO remove handler mapping ?
                            }
                            if (copyMap.get(scriptLocation) != scriptLastModified(scriptLocation)) {
                                registerGroovyController(scriptLocation);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //ignore
                    }
                }
            }
        }.start();
    }


    private long scriptLastModified(String scriptLocation) {
        try {
            return ctx.getResource(scriptLocation).getFile().lastModified();
        } catch (Exception e) {
            return -1;
        }
    }

    private boolean scriptNotExists(String scriptLocation) {
        return !ctx.getResource(scriptLocation).exists();
    }

}
