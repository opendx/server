package com.yqhp.typehandler;

import com.yqhp.mbg.po.Action;

/**
 * Created by jiangyitao.
 */
public class ActionListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return Action.class;
    }
}
