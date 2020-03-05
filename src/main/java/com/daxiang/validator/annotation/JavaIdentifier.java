package com.daxiang.validator.annotation;

import com.daxiang.validator.JavaIdentifierValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by jiangyitao.
 */
@Documented
@Constraint(validatedBy = JavaIdentifierValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JavaIdentifier {
    String message() ;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
