package com.daxiang.dao;

import com.daxiang.mbg.po.Action;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface ActionDao {
    List<Action> selectOtherActionsInUse(@Param("actionId") Integer actionId);

    List<Action> selectByLocalVarsEnvironmentId(@Param("envId") Integer envId);

    List<Action> selectPublishedCascaderData(@Param("projectId") Integer projectId, @Param("platform") Integer platform, @Param("type") Integer type);

    int insertBasicActions(@Param("actions") List<Action> actions);
}
