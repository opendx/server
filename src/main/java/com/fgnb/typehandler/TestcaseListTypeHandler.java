package com.fgnb.typehandler;

import com.fgnb.model.vo.Testcase;

/**
 * Created by jiangyitao.
 */
public class TestcaseListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return Testcase.class;
    }
}
