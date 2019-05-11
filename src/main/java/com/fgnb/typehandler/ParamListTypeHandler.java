package com.fgnb.typehandler;

import com.fgnb.model.action.Param;

/**
 * Created by jiangyitao.
 */
public class ParamListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return Param.class;
    }
}
