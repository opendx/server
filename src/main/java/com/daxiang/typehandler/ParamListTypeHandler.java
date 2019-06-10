package com.daxiang.typehandler;

import com.daxiang.model.action.Param;

/**
 * Created by jiangyitao.
 */
public class ParamListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return Param.class;
    }
}
