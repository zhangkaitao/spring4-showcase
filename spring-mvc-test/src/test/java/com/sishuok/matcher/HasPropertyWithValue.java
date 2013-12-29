package com.sishuok.matcher;

import org.hamcrest.*;
import org.springframework.beans.BeanWrapperImpl;

import static org.hamcrest.Condition.matched;
import static org.hamcrest.Condition.notMatched;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-29
 * <p>Version: 1.0
 */
public class HasPropertyWithValue<T> extends TypeSafeDiagnosingMatcher<T> {
    private final String propertyNames;
    private final Matcher<Object> valueMatcher;

    public HasPropertyWithValue(String propertyNames, Matcher<?> valueMatcher) {
        this.propertyNames = propertyNames;
        this.valueMatcher = nastyGenericsWorkaround(valueMatcher);
    }

    @Override
    public boolean matchesSafely(T bean, Description mismatch) {
        return withPropertyValue(bean, mismatch)
                .matching(valueMatcher, "property '" + propertyNames + "' ");
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("hasProperty(").appendValue(propertyNames).appendText(", ")
                .appendDescriptionOf(valueMatcher).appendText(")");
    }

    private Condition<Object> withPropertyValue(T bean, Description mismatch) {
        try {
            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(bean);
            Object property = beanWrapper.getPropertyValue(propertyNames);
            if (property == null) {
                mismatch.appendText("No property \"" + propertyNames + "\"");
                return notMatched();
            }

            return matched(property, mismatch);

        } catch (Exception e) {
            mismatch.appendText(e.getMessage());
            return notMatched();
        }

    }


    @SuppressWarnings("unchecked")
    private static Matcher<Object> nastyGenericsWorkaround(Matcher<?> valueMatcher) {
        return (Matcher<Object>) valueMatcher;
    }

    @Factory
    public static <T> Matcher<T> hasProperty(String propertyName, Matcher<?> valueMatcher) {
        return new HasPropertyWithValue<T>(propertyName, valueMatcher);
    }
}
