package com.fgnb.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.fgnb.dao.TestSuiteDao;
import com.fgnb.mbg.mapper.TestSuiteMapper;
import com.fgnb.mbg.po.TestSuite;
import com.fgnb.model.Page;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.model.vo.TestSuiteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by jiangyitao.
 */
@Service
public class TestSuiteService extends BaseService {

    @Autowired
    private TestSuiteMapper testSuiteMapper;
    @Autowired
    private TestSuiteDao testSuiteDao;

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

    public Response list(TestSuite testSuite, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();

        if(needPaging) {
            //分页
            PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        }
        List<TestSuiteVo> testSuiteVos = testSuiteDao.selectByTestSuite(testSuite);
        if(needPaging) {
            return Response.success(Page.convert(new PageInfo(testSuiteVos)));
        }
        return Response.success(testSuiteVos);
    }
}
