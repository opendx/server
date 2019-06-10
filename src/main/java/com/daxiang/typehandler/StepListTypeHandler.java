package com.daxiang.typehandler;

import com.daxiang.model.action.Step;

/**
 * Created by jiangyitao.
 */
public class StepListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return Step.class;
    }
}
