package com.fgnb.dao;

import com.fgnb.mbg.po.TestTask;
import com.fgnb.model.vo.TestTaskVo;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface TestTaskDao {
    List<TestTaskVo> selectByTestTask(TestTask testTask);
}
