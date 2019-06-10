package com.daxiang.typehandler;

import com.daxiang.model.action.LocalVar;

/**
 * Created by jiangyitao.
 */
public class LocalVarListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return LocalVar.class;
    }
}
