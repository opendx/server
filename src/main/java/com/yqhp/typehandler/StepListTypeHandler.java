package com.yqhp.typehandler;

import com.yqhp.model.action.Step;

/**
 * Created by jiangyitao.
 */
public class StepListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return Step.class;
    }
}
