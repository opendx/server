package com.yqhp.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yqhp.dao.GlobalVarDao;
import com.yqhp.mbg.mapper.GlobalVarMapper;
import com.yqhp.mbg.po.GlobalVar;
import com.yqhp.mbg.po.GlobalVarExample;
import com.yqhp.model.PageRequest;
import com.yqhp.model.Response;
import com.yqhp.model.vo.GlobalVarVo;
import com.yqhp.model.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
        //分页排序
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize(), "v.create_time desc");
        List<GlobalVarVo> globalVarVos = globalVarDao.selectByGlobalVar(globalVar);
        return Response.success(PageVo.convert(new PageInfo(globalVarVos)));
    }

    /**
     * 根据项目id查出全局变量
     *
     * @param projectId
     * @return
     */
    public Response findByProjectId(Integer projectId) {
        if (projectId == null) {
            return Response.fail("项目id不能为空");
        }
        //根据id查询全局变量
        GlobalVarExample globalVarExample = new GlobalVarExample();
        globalVarExample.createCriteria().andProjectIdEqualTo(projectId);
        globalVarExample.setOrderByClause("create_time desc");
        List<GlobalVar> globalVars = globalVarMapper.selectByExample(globalVarExample);
        return Response.success(globalVars);
    }
}
