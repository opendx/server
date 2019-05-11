package com.fgnb.typehandler;

import com.fgnb.model.action.Step;

/**
 * Created by jiangyitao.
 */
public class StepListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return Step.class;
    }
}
