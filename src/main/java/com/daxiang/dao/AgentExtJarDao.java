package com.daxiang.dao;

import com.daxiang.mbg.po.AgentExtJar;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface AgentExtJarDao {
    List<AgentExtJar> selectLastUploadTimeList();
}
