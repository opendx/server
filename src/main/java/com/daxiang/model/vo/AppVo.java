package com.daxiang.model.vo;

import com.daxiang.mbg.po.App;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class AppVo extends App {
    private String uploadorNickName;

    public static AppVo convert(App app, String uploadorNickName) {
        AppVo appVo = new AppVo();
        BeanUtils.copyProperties(app, appVo);
        appVo.setUploadorNickName(uploadorNickName);
        return appVo;
    }
}
