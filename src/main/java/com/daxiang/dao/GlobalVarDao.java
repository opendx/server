package com.daxiang.dao;

import com.daxiang.mbg.po.GlobalVar;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface GlobalVarDao {
    int insertBatch(@Param("globalVars") List<GlobalVar> globalVars);
    List<GlobalVar> selectByEnvironmentId(@Param("envId") Integer envId);
}
