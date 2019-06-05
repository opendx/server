package com.fgnb.typehandler;

import com.fgnb.mbg.po.GlobalVar;

/**
 * Created by jiangyitao.
 */
public class GlobalVarListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return GlobalVar.class;
    }
}
