package com.yqhp.service;

import com.yqhp.mbg.mapper.TestSuiteMapper;
import com.yqhp.mbg.po.TestSuite;
import com.yqhp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by jiangyitao.
 */
@Service
public class TestSuiteService extends BaseService {

    @Autowired
    private TestSuiteMapper testSuiteMapper;

    public Response add(TestSuite testSuite) {

        testSuite.setCreateTime(new Date());
        testSuite.setCreatorUid(getUid());

        try {
            int insertRow = testSuiteMapper.insertSelective(testSuite);
            if (insertRow != 1) {
                return Response.fail("添加测试集失败");
            }
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        return Response.success("添加成功");
    }

    public Response delete(Integer testSuiteId) {
        if (testSuiteId == null) {
            return Response.fail("id不能为空");
        }

        int deleteRow = testSuiteMapper.deleteByPrimaryKey(testSuiteId);
        if (deleteRow != 1) {
            return Response.fail("删除失败");
        }

        return Response.success("删除成功");
    }

    public Response update(TestSuite testSuite) {
        if (testSuite.getId() == null) {
            return Response.fail("测试集id不能为空");
        }

        try {
            int updateRow = testSuiteMapper.updateByPrimaryKeySelective(testSuite);
            if (updateRow != 1) {
                return Response.fail("更新失败");
            }
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        return Response.success("更新测试集成功");
    }
}
