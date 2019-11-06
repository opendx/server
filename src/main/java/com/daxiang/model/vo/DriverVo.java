package com.daxiang.model.vo;

import com.daxiang.mbg.po.Driver;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class DriverVo extends Driver {
    private String creatorNickName;

    public static DriverVo convert(Driver driver, String creatorNickName) {
        DriverVo driverVo = new DriverVo();
        BeanUtils.copyProperties(driver, driverVo);
        driverVo.setCreatorNickName(creatorNickName);
        return driverVo;
    }
}
