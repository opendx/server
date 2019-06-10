package com.daxiang.typehandler;

import com.daxiang.mbg.po.GlobalVar;

/**
 * Created by jiangyitao.
 */
public class GlobalVarListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return GlobalVar.class;
    }
}
