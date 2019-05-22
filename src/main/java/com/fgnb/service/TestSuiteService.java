package com.fgnb.service;

import com.fgnb.mbg.po.TestSuiteExample;
import com.fgnb.model.UserCache;
import com.github.pagehelper.PageHelper;
import com.fgnb.mbg.mapper.TestSuiteMapper;
import com.fgnb.mbg.po.TestSuite;
import com.fgnb.model.Page;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.model.vo.TestSuiteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public Response list(TestSuite testSuite, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if(needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        }

        List<TestSuite> testSuites = selectByTestSuite(testSuite);
        List<TestSuiteVo> testSuiteVos = testSuites.stream().map(s -> TestSuiteVo.convert(s, UserCache.getNickNameById(s.getCreatorUid()))).collect(Collectors.toList());

        if(needPaging) {
            // java8 stream会导致PageHelper total计算错误
            // 所以这里用testSuites计算total
            long total = Page.getTotal(testSuites);
            return Response.success(Page.build(testSuiteVos,total));
        } else {
            return Response.success(testSuiteVos);
        }
    }

    public List<TestSuite> selectByTestSuite(TestSuite testSuite) {
        if (testSuite == null) {
            testSuite = new TestSuite();
        }

        TestSuiteExample testSuiteExample = new TestSuiteExample();
        TestSuiteExample.Criteria criteria = testSuiteExample.createCriteria();

        if(testSuite.getId() != null) {
            criteria.andIdEqualTo(testSuite.getId());
        }
        if(testSuite.getProjectId() != null) {
            criteria.andProjectIdEqualTo(testSuite.getProjectId());
        }
        if(!StringUtils.isEmpty(testSuite.getName())) {
            criteria.andNameEqualTo(testSuite.getName());
        }

        return testSuiteMapper.selectByExample(testSuiteExample);
    }

}
