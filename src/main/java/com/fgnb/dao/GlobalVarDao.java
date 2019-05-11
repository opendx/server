package com.fgnb.dao;

import com.fgnb.mbg.po.GlobalVar;
import com.fgnb.model.vo.GlobalVarVo;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface GlobalVarDao {
    List<GlobalVarVo> selectByGlobalVar(GlobalVar globalVar);
}
