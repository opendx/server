package com.yqhp.typehandler;

import com.yqhp.model.action.LocalVar;

/**
 * Created by jiangyitao.
 */
public class LocalVarListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return LocalVar.class;
    }
}
