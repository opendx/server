package com.yqhp.typehandler;

import com.yqhp.model.vo.Testcase;

/**
 * Created by jiangyitao.
 */
public class TestcaseListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return Testcase.class;
    }
}
