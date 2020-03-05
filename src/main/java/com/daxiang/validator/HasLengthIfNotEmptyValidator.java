package com.daxiang.validator;

import com.daxiang.validator.annotation.HasLengthIfNotEmpty;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * Created by jiangyitao.
 */
public class HasLengthIfNotEmptyValidator implements ConstraintValidator<HasLengthIfNotEmpty, List<String>> {
    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        if (!CollectionUtils.isEmpty(values)) {
            for (String value : values) {
                if (!StringUtils.hasLength(value)) {
                    return false;
                }
            }
        }
        return true;
    }
}
