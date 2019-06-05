package com.fgnb.model.vo;

import com.fgnb.mbg.po.TestSuite;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class TestSuiteVo extends TestSuite {
    private String creatorNickName;

    public static TestSuiteVo convert(TestSuite testSuite, String creatorNickName) {
        TestSuiteVo testSuiteVo = new TestSuiteVo();
        BeanUtils.copyProperties(testSuite, testSuiteVo);
        testSuiteVo.setCreatorNickName(creatorNickName);
        return testSuiteVo;
    }
}
