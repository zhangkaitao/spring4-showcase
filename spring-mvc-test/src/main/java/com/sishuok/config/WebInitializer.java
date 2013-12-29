package com.sishuok.config;

/**
 * 请注释掉web.xml配置后使用
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-17
 * <p>Version: 1.0
 */
//public class WebInitializer implements WebApplicationInitializer {
//
//    @Override
//    public void onStartup(javax.servlet.ServletContext sc) throws ServletException {
//
//        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
//        rootContext.register(AppConfig.class);
//        sc.addListener(new ContextLoaderListener(rootContext));
//
//        //2、springmvc上下文
//        AnnotationConfigWebApplicationContext springMvcContext = new AnnotationConfigWebApplicationContext();
//        springMvcContext.register(MvcConfig.class);
//        //3、DispatcherServlet
//        DispatcherServlet dispatcherServlet = new DispatcherServlet(springMvcContext);
//        ServletRegistration.Dynamic dynamic = sc.addServlet("dispatcherServlet", dispatcherServlet);
//        dynamic.setLoadOnStartup(1);
//        dynamic.addMapping("/");
//
//        //4、CharacterEncodingFilter
//        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//        characterEncodingFilter.setEncoding("utf-8");
//        FilterRegistration filterRegistration =
//                sc.addFilter("characterEncodingFilter", characterEncodingFilter);
//        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/");
//
//    }
//}
