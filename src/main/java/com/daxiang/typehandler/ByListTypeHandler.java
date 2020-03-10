package com.daxiang.typehandler;

import com.daxiang.model.page.By;

/**
 * Created by jiangyitao.
 */
public class ByListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return By.class;
    }
}
