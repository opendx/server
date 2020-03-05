package com.daxiang.validator;

import com.daxiang.validator.annotation.JavaIdentifier;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by jiangyitao.
 * 校验是否符合java标识符规范
 */
public class JavaIdentifierValidator implements ConstraintValidator<JavaIdentifier, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isJavaIdentifier(value);
    }

    public static boolean isJavaIdentifier(String value) {
        if (!StringUtils.hasLength(value)) {
            return false;
        }

        // 校验首字符
        if (!Character.isJavaIdentifierStart(value.charAt(0))) {
            return false;
        }

        // 校验首字符之后的字符
        for (int i = 1; i < value.length(); i++) {
            if (!Character.isJavaIdentifierPart(value.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
