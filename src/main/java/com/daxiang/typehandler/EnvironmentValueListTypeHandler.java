package com.daxiang.typehandler;

import com.daxiang.model.environment.EnvironmentValue;

/**
 * Created by jiangyitao.
 */
public class EnvironmentValueListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return EnvironmentValue.class;
    }
}
