package com.daxiang.service;

import com.daxiang.mbg.mapper.EnvironmentMapper;
import com.daxiang.mbg.po.Environment;
import com.daxiang.mbg.po.EnvironmentExample;
import com.daxiang.model.Page;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.UserCache;
import com.daxiang.model.vo.EnvironmentVo;
import com.github.pagehelper.PageHelper;
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
public class EnvironmentService extends BaseService {

    @Autowired
    private EnvironmentMapper environmentMapper;

    public Response add(Environment environment) {
        environment.setCreateTime(new Date());
        environment.setCreatorUid(getUid());

        int insertRow;
        try {
            insertRow = environmentMapper.insertSelective(environment);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return insertRow == 1 ? Response.success("添加成功") : Response.fail("添加失败，请稍后重试");
    }

    public Response delete(Integer environmentId) {
        if (environmentId == null) {
            return Response.fail("环境id不能为空");
        }
        // todo 被使用不能删除

        int deleteRow = environmentMapper.deleteByPrimaryKey(environmentId);
        return deleteRow == 1 ? Response.success("删除成功") : Response.fail("删除失败，请稍后重试");
    }

    public Response update(Environment environment) {
        int updateRow;
        try {
            updateRow = environmentMapper.updateByPrimaryKeySelective(environment);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return updateRow == 1 ? Response.success("更新成功") : Response.fail("修改失败,请稍后重试");
    }

    public Response list(Environment environment, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<Environment> environments = selectByEnvironment(environment);
        List<EnvironmentVo> environmentVos = environments.stream().map(e -> EnvironmentVo.convert(e, UserCache.getNickNameById(e.getCreatorUid()))).collect(Collectors.toList());

        if (needPaging) {
            // java8 stream会导致PageHelper total计算错误
            // 所以这里用environments计算total
            long total = Page.getTotal(environments);
            return Response.success(Page.build(environmentVos, total));
        } else {
            return Response.success(environmentVos);
        }
    }

    public List<Environment> selectByEnvironment(Environment environment) {
        EnvironmentExample example = new EnvironmentExample();
        EnvironmentExample.Criteria criteria = example.createCriteria();

        if (environment != null) {
            if (environment.getId() != null) {
                criteria.andIdEqualTo(environment.getId());
            }
            if (!StringUtils.isEmpty(environment.getName())) {
                criteria.andNameEqualTo(environment.getName());
            }
            if (environment.getProjectId() != null) {
                criteria.andProjectIdEqualTo(environment.getProjectId());
            }
        }
        example.setOrderByClause("create_time desc");

        return environmentMapper.selectByExample(example);
    }

    public Environment selectByPrimaryKey(Integer id) {
        return environmentMapper.selectByPrimaryKey(id);
    }
}
