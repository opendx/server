package com.daxiang.validator;

import com.daxiang.model.page.Element;
import com.daxiang.validator.annotation.NoDuplicateElement;
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
public class NoDuplicateElementValidator implements ConstraintValidator<NoDuplicateElement, List<Element>> {
    @Override
    public boolean isValid(List<Element> elementList, ConstraintValidatorContext context) {
        if (!CollectionUtils.isEmpty(elementList)) {
            Map<String, Long> elementNameCountMap = elementList.stream().filter(element -> !StringUtils.isEmpty(element.getName()))
                    .collect(Collectors.groupingBy(Element::getName, Collectors.counting()));
            for (Map.Entry<String, Long> elementNameCount : elementNameCountMap.entrySet()) {
                if (elementNameCount.getValue() > 1) {
                    return false;
                }
            }
        }
        return true;
    }
}
