package com.daxiang.model.vo;

import com.daxiang.mbg.po.TestTask;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class TestTaskVo extends TestTask {
    private String creatorNickName;

    public static TestTaskVo convert(TestTask testTask, String creatorNickName) {
        TestTaskVo testTaskVo = new TestTaskVo();
        BeanUtils.copyProperties(testTask, testTaskVo);
        testTaskVo.setCreatorNickName(creatorNickName);
        return testTaskVo;
    }
}
