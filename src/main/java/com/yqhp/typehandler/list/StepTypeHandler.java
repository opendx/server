package com.yqhp.typehandler.list;

import com.yqhp.model.action.Step;

/**
 * Created by jiangyitao.
 */
public class StepTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return Step.class;
    }
}
