package com.sishuok.spring4.validator;

import org.hibernate.validator.constraints.Length;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-15
 * <p>Version: 1.0
 */

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@NotNull(message = "{user.name.null}")
@Length(min = 5, max = 20, message = "{user.name.length.illegal}")
@Pattern(regexp = "[a-zA-Z]{5,20}", message = "{user.name.length.illegal}")
@Constraint(validatedBy = {})
public @interface Composition {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
