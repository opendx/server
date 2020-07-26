package com.daxiang.service;

import com.daxiang.mbg.mapper.ActionMapper;
import com.daxiang.model.action.ParamValue;
import com.daxiang.model.action.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 * 数据迁移
 */
@Service
public class DataMigrationService {

    @Autowired
    private ActionMapper actionMapper;

    /**
     * todo 后续删除
     * v0.7.5 将step.paramValues -> step.args
     */
    public void actionStepParamValuesToArgs() {
        actionMapper.selectByExampleWithBLOBs(null).stream()
                .filter(action -> !CollectionUtils.isEmpty(action.getSteps()))
                .forEach(action -> {
                    List<Step> steps = action.getSteps();
                    for (Step step : steps) {
                        List<ParamValue> paramValues = step.getParamValues();
                        if (CollectionUtils.isEmpty(paramValues)) {
                            step.setArgs(new ArrayList<>());
                        } else {
                            List<String> values = paramValues.stream().map(ParamValue::getParamValue).collect(Collectors.toList());
                            step.setArgs(values);
                        }
                    }
                    actionMapper.updateByPrimaryKeySelective(action);
                });
    }
}
