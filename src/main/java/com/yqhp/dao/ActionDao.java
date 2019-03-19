package com.yqhp.dao;

import com.yqhp.mbg.po.Action;
import com.yqhp.model.vo.ActionVo;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface ActionDao {

    List<ActionVo> selectByAction(Action action);
}
