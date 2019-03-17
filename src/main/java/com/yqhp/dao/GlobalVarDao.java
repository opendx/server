package com.yqhp.dao;

import com.yqhp.mbg.po.GlobalVar;
import com.yqhp.model.vo.GlobalVarVo;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface GlobalVarDao {
    List<GlobalVarVo> selectByGlobalVar(GlobalVar globalVar);
}
