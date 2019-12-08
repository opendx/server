package com.daxiang.typehandler;

import com.daxiang.model.page.Element;

/**
 * Created by jiangyitao.
 */
public class ElementListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return Element.class;
    }
}
