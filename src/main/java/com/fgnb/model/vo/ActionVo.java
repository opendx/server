package com.fgnb.model.vo;

import com.fgnb.mbg.po.Action;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class ActionVo extends Action {
    private String creatorNickName;
    private String updatorNickName;

    public static ActionVo convert(Action action, String creatorNickName, String updatorNickName) {
        ActionVo actionVo = new ActionVo();
        BeanUtils.copyProperties(action, actionVo);
        actionVo.setCreatorNickName(creatorNickName);
        actionVo.setUpdatorNickName(updatorNickName);
        return actionVo;
    }
}
