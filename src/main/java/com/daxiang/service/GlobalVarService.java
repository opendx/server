package com.daxiang.service;

import com.daxiang.dao.GlobalVarDao;
import com.daxiang.mbg.po.GlobalVarExample;
import com.daxiang.model.UserCache;
import com.github.pagehelper.PageHelper;
import com.daxiang.mbg.mapper.GlobalVarMapper;
import com.daxiang.mbg.po.GlobalVar;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.vo.GlobalVarVo;
import com.daxiang.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
public class GlobalVarService extends BaseService {

    @Autowired
    private GlobalVarMapper globalVarMapper;
    @Autowired
    private GlobalVarDao globalVarDao;

    /**
     * 添加全局变量
     *
     * @param globalVar
     * @return
     */
    public Response add(GlobalVar globalVar) {
        globalVar.setCreateTime(new Date());
        globalVar.setCreatorUid(getUid());

        int insertRow;
        try {
            insertRow = globalVarMapper.insertSelective(globalVar);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return insertRow == 1 ? Response.success("添加GlobalVar成功") : Response.fail("添加GlobalVar失败，请稍后重试");
    }

    /**
     * 删除全局变量
     *
     * @param globalVarId
     */
    public Response delete(Integer globalVarId) {
        if (globalVarId == null) {
            return Response.fail("globalVarId不能为空");
        }

        // todo 检查全局变量是否被action使用
        int deleteRow = globalVarMapper.deleteByPrimaryKey(globalVarId);
        return deleteRow == 1 ? Response.success("删除成功") : Response.fail("删除失败,请稍后重试");
    }

    /**
     * 修改全局变量
     *
     * @param globalVar
     */
    public Response update(GlobalVar globalVar) {
        // todo 检查全局变量是否被action使用，目前是通过前端限制修改name

        int updateRow;
        try {
            updateRow = globalVarMapper.updateByPrimaryKeySelective(globalVar);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        return updateRow == 1 ? Response.success("更新GlobalVar成功") : Response.fail("更新GlobalVar失败,请稍后重试");
    }

    /**
     * 查询全局变量列表
     *
     * @param globalVar
     * @return
     */
    public Response list(GlobalVar globalVar, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<GlobalVar> globalVars = selectByGlobalVar(globalVar);
        List<GlobalVarVo> globalVarVos = globalVars.stream().map(g -> GlobalVarVo.convert(g, UserCache.getNickNameById(g.getCreatorUid()))).collect(Collectors.toList());

        if (needPaging) {
            // java8 stream会导致PageHelper total计算错误
            // 所以这里用globalVars计算total
            long total = Page.getTotal(globalVars);
            return Response.success(Page.build(globalVarVos, total));
        } else {
            return Response.success(globalVarVos);
        }
    }

    public List<GlobalVar> selectByGlobalVar(GlobalVar globalVar) {
        GlobalVarExample example = new GlobalVarExample();
        GlobalVarExample.Criteria criteria = example.createCriteria();

        if (globalVar != null) {
            if (globalVar.getId() != null) {
                criteria.andIdEqualTo(globalVar.getId());
            }
            if (globalVar.getType() != null) {
                criteria.andTypeEqualTo(globalVar.getType());
            }
            if (globalVar.getProjectId() != null) {
                criteria.andProjectIdEqualTo(globalVar.getProjectId());
            }
            if (!StringUtils.isEmpty(globalVar.getName())) {
                criteria.andNameEqualTo(globalVar.getName());
            }
        }
        example.setOrderByClause("create_time desc");

        return globalVarMapper.selectByExampleWithBLOBs(example);
    }

    public List<GlobalVar> selectByEnvironmentId(Integer envId) {
        if (envId == null) {
            return Collections.EMPTY_LIST;
        }
        return globalVarDao.selectByEnvironmentId(envId);
    }
}
