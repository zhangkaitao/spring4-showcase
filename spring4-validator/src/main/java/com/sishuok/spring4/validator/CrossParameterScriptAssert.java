package com.sishuok.spring4.validator;

import javax.validation.Constraint;
import javax.validation.ConstraintTarget;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-15
 * <p>Version: 1.0
 */
@Constraint(validatedBy = {
        CrossParameterScriptAssertClassValidator.class,
        CrossParameterScriptAssertParameterValidator.class
})
@Target({TYPE, FIELD, PARAMETER, METHOD, CONSTRUCTOR, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
public @interface CrossParameterScriptAssert {
    String message() default "error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String script();

    String lang();

    String alias() default "_this";

    String property() default "";

    ConstraintTarget validationAppliesTo() default ConstraintTarget.IMPLICIT;
}
