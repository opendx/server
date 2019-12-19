package com.daxiang.dao;

import com.daxiang.mbg.po.Action;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface ActionDao {
    List<Action> selectByActionIdInStepsOrDepends(@Param("actionId") Integer actionId);

    List<Action> selectByLocalVarsEnvironmentId(@Param("envId") Integer envId);

    List<Action> selectByProjectIdAndPlatform(@Param("projectId") Integer projectId, @Param("platform") Integer platform);
}
