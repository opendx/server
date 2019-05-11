package com.fgnb.typehandler;

import com.fgnb.model.testplan.Before;

/**
 * Created by jiangyitao.
 */
public class BeforeListTypeHandler extends ListTypeHandler{
    @Override
    public Class getTypeClass() {
        return Before.class;
    }
}
