package com.daxiang.validator;

import com.alibaba.fastjson.JSONObject;
import com.daxiang.validator.annotation.JSONFormatIfHasText;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by jiangyitao.
 */
public class JSONFormatIfHasTextValidator implements ConstraintValidator<JSONFormatIfHasText, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.hasText(value)) {
            try {
                JSONObject.parseObject(value);
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }
}
