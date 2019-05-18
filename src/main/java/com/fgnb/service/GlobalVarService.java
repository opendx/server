package com.fgnb.service;

import com.fgnb.mbg.po.GlobalVarExample;
import com.fgnb.model.UserCache;
import com.github.pagehelper.PageHelper;
import com.fgnb.mbg.mapper.GlobalVarMapper;
import com.fgnb.mbg.po.GlobalVar;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.model.vo.GlobalVarVo;
import com.fgnb.model.Page;
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
public class GlobalVarService extends BaseService {

    @Autowired
    private GlobalVarMapper globalVarMapper;

    /**
     * 添加全局变量
     *
     * @param globalVar
     * @return
     */
    public Response add(GlobalVar globalVar) {
        globalVar.setCreateTime(new Date());
        globalVar.setCreatorUid(getUid());

        try {
            int insertRow = globalVarMapper.insertSelective(globalVar);
            if (insertRow != 1) {
                return Response.fail("添加全局变量失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return Response.success("添加成功");
    }

    /**
     * 删除全局变量
     *
     * @param globalVarId
     */
    public Response delete(Integer globalVarId) {
        if (globalVarId == null) {
            return Response.fail("变量id不能为空");
        }
        // todo 校验全局变量是否被其他action使用，如果被使用则不能直接删除
        int deleteRow = globalVarMapper.deleteByPrimaryKey(globalVarId);
        if (deleteRow != 1) {
            return Response.fail("删除失败,请稍后重试");
        }
        return Response.success("删除成功");
    }

    /**
     * 修改全局变量
     *
     * @param globalVar
     */
    public Response update(GlobalVar globalVar) {
        if (globalVar.getId() == null) {
            return Response.fail("全局变量id不能为空");
        }
        // todo 校验name是否被action使用，如果被使用则不能直接修改name
        // 先在前端限制无法修改name
        try {
            int updateRow = globalVarMapper.updateByPrimaryKeySelective(globalVar);
            if (updateRow != 1) {
                return Response.fail("更新全局变量失败,请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        return Response.success("更新成功");
    }

    /**
     * 查询全局变量列表
     *
     * @param globalVar
     * @return
     */
    public Response list(GlobalVar globalVar, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if(needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<GlobalVar> globalVars = selectByGlobalVar(globalVar);
        List<GlobalVarVo> globalVarVos = globalVars.stream().map(g -> GlobalVarVo.convert(g, UserCache.getNickNameById(g.getCreatorUid()))).collect(Collectors.toList());

        if(needPaging) {
            // java8 stream会导致PageHelper total计算错误
            // 所以这里用globalVars计算total
            long total = Page.getTotal(globalVars);
            return Response.success(Page.build(globalVarVos,total));
        } else {
            return Response.success(globalVarVos);
        }
    }

    public List<GlobalVar> selectByGlobalVar(GlobalVar globalVar) {
        if (globalVar == null) {
            globalVar = new GlobalVar();
        }

        GlobalVarExample globalVarExample = new GlobalVarExample();
        GlobalVarExample.Criteria criteria = globalVarExample.createCriteria();

        if(globalVar.getId() != null) {
            criteria.andIdEqualTo(globalVar.getId());
        }
        if(globalVar.getProjectId() != null) {
            criteria.andProjectIdEqualTo(globalVar.getProjectId());
        }
        if(!StringUtils.isEmpty(globalVar.getName())) {
            criteria.andNameEqualTo(globalVar.getName());
        }
        if(!StringUtils.isEmpty(globalVar.getValue())) {
            criteria.andValueEqualTo(globalVar.getValue());
        }

        globalVarExample.setOrderByClause("create_time desc");
        return globalVarMapper.selectByExample(globalVarExample);
    }
}
