package com.daxiang.typehandler;

import com.daxiang.mbg.po.Page;

/**
 * Created by jiangyitao.
 */
public class PageListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return Page.class;
    }
}
