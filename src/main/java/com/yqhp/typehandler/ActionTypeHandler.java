package com.yqhp.typehandler;

import com.yqhp.mbg.po.Action;

/**
 * Created by jiangyitao.
 */
public class ActionTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return Action.class;
    }
}
