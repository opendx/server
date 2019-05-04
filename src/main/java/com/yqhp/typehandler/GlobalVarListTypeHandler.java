package com.yqhp.typehandler;

import com.yqhp.mbg.po.GlobalVar;

/**
 * Created by jiangyitao.
 */
public class GlobalVarListTypeHandler extends ListTypeHandler{
    @Override
    public Class getTypeClass() {
        return GlobalVar.class;
    }
}
