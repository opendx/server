package com.daxiang.typehandler;

import com.daxiang.model.dto.Testcase;

/**
 * Created by jiangyitao.
 */
public class TestcaseListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return Testcase.class;
    }
}
