package com.yqhp.model.vo;

import com.yqhp.mbg.po.Action;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class ActionVo extends Action {
    private String creatorNickName;
    private String updatorNickName;
}
