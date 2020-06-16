package com.daxiang.validator;

import com.daxiang.model.page.By;
import com.daxiang.validator.annotation.NoDuplicateBy;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
public class NoDuplicateByValidator implements ConstraintValidator<NoDuplicateBy, List<By>> {
    @Override
    public boolean isValid(List<By> byList, ConstraintValidatorContext context) {
        if (!CollectionUtils.isEmpty(byList)) {
            Map<String, Long> byNameCountMap = byList.stream().filter(by -> !StringUtils.isEmpty(by.getName()))
                    .collect(Collectors.groupingBy(By::getName, Collectors.counting()));
            for (Map.Entry<String, Long> byNameCount : byNameCountMap.entrySet()) {
                if (byNameCount.getValue() > 1) {
                    return false;
                }
            }
        }
        return true;
    }
}
