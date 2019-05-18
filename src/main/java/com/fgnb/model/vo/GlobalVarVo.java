package com.fgnb.model.vo;

import com.fgnb.mbg.po.GlobalVar;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class GlobalVarVo extends GlobalVar{
    private String creatorNickName;

    public static GlobalVarVo convert(GlobalVar globalVar,String creatorNickName) {
        GlobalVarVo globalVarVo = new GlobalVarVo();
        BeanUtils.copyProperties(globalVar,globalVarVo);
        globalVarVo.setCreatorNickName(creatorNickName);
        return globalVarVo;
    }
}
