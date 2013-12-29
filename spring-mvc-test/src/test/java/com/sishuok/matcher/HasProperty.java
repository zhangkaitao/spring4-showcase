package com.sishuok.matcher;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.beans.BeanWrapperImpl;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-29
 * <p>Version: 1.0
 */
public class HasProperty<T> extends TypeSafeMatcher<T> {

    private String propertyNames;

    public HasProperty(String propertyNames) {
        this.propertyNames = propertyNames;
    }


    @Override
    public boolean matchesSafely(T obj) {
        try {
            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(obj);
            return beanWrapper.getPropertyValue(propertyNames) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void describeMismatchSafely(T item, Description mismatchDescription) {
        mismatchDescription.appendText("no ").appendValue(propertyNames).appendText(" in ").appendValue(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("hasProperty(").appendValue(propertyNames).appendText(")");
    }

    @Factory
    public static <T> Matcher<T> hasProperty(String propertyNames) {
        return new HasProperty<T>(propertyNames);
    }


}
