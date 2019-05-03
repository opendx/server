package com.yqhp.typehandler;

import com.yqhp.model.action.Param;

/**
 * Created by jiangyitao.
 */
public class ParamListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return Param.class;
    }
}
