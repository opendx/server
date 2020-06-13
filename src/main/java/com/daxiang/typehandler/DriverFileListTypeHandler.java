package com.daxiang.typehandler;

import com.daxiang.model.dto.DriverFile;

/**
 * Created by jiangyitao.
 */
public class DriverFileListTypeHandler extends ListTypeHandler {
    @Override
    public Class getTypeClass() {
        return DriverFile.class;
    }
}
