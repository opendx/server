package com.yqhp.typehandler.list;

import com.yqhp.model.action.LocalVar;

/**
 * Created by jiangyitao.
 */
public class LocalVarTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return LocalVar.class;
    }
}
