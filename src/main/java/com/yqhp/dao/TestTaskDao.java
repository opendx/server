package com.yqhp.dao;

import com.yqhp.mbg.po.TestTask;
import com.yqhp.model.vo.TestTaskVo;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface TestTaskDao {
    List<TestTaskVo> selectByTestTask(TestTask testTask);
}
