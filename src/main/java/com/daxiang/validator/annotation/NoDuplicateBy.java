package com.daxiang.validator.annotation;

import com.daxiang.validator.NoDuplicateByValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by jiangyitao.
 */
@Documented
@Constraint(validatedBy = NoDuplicateByValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDuplicateBy {
    String message() ;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
