package com.yqhp.typehandler;

import com.yqhp.model.action.Param;

/**
 * Created by jiangyitao.
 */
public class ParamTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return Param.class;
    }
}
