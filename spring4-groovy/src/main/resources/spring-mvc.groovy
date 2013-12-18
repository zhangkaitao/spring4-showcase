import org.hibernate.validator.HibernateValidator
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.view.InternalResourceViewResolver

beans {
    xmlns context: "http://www.springframework.org/schema/context"
    xmlns mvc: "http://www.springframework.org/schema/mvc"

    context.'component-scan'('base-package': "com,sishuok.spring4")
    mvc.'annotation-driven'('validator': "validator")

    validator(LocalValidatorFactoryBean) {
        providerClass = HibernateValidator.class
        validationMessageSource = ref("messageSource")
    }

    messageSource(ReloadableResourceBundleMessageSource) {
        basenames = ["classpath:messages", "classpath:org/hibernate/validator/ValidationMessages"]
        defaultEncoding = "UTF-8"
        cacheSeconds = 60
    }


    viewResolver(InternalResourceViewResolver) {
        prefix = "/WEB-INF/jsp/"
        suffix = ".jsp"
    }

    mvc.interceptors() {
        localeChangeInterceptor(LocaleChangeInterceptor) {
            paramName = "language"
        }
    }


    cookieLocaleResolver(CookieLocaleResolver) {
        cookieName = "language"
        cookieMaxAge = "3600"
        defaultLocale = "zh_CN"
    }



    importBeans "classpath:spring-import.groovy"


}

