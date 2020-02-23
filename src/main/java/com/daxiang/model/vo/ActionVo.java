package com.daxiang.model.vo;

import com.daxiang.mbg.po.Action;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class ActionVo extends Action {
    private String creatorNickName = "";
    private String updatorNickName = "";
}
