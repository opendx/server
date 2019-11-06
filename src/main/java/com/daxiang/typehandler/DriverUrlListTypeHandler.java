package com.daxiang.typehandler;

import com.daxiang.model.vo.DriverUrl;

/**
 * Created by jiangyitao.
 */
public class DriverUrlListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return DriverUrl.class;
    }
}
