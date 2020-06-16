package com.daxiang.validator;

import com.daxiang.model.environment.EnvironmentValue;
import com.daxiang.validator.annotation.NoDuplicateEnvironment;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
public class NoDuplicateEnvironmentValidator implements ConstraintValidator<NoDuplicateEnvironment, List<EnvironmentValue>> {
    @Override
    public boolean isValid(List<EnvironmentValue> environmentValueList, ConstraintValidatorContext context) {
        if (!CollectionUtils.isEmpty(environmentValueList)) {
            Map<Integer, Long> environmentIdCountMap = environmentValueList.stream().filter(env -> Objects.nonNull(env.getEnvironmentId()))
                    .collect(Collectors.groupingBy(EnvironmentValue::getEnvironmentId, Collectors.counting()));
            for (Map.Entry<Integer, Long> environmentIdCount : environmentIdCountMap.entrySet()) {
                if (environmentIdCount.getValue() > 1) {
                    return false;
                }
            }
        }
        return true;
    }
}
