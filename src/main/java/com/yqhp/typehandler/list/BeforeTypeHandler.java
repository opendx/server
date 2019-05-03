package com.yqhp.typehandler.list;

import com.yqhp.model.testplan.Before;

/**
 * Created by jiangyitao.
 */
public class BeforeTypeHandler extends ListTypeHandler{
    @Override
    public Class getTypeClass() {
        return Before.class;
    }
}
