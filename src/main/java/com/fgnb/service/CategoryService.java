package com.fgnb.service;

import com.fgnb.mbg.mapper.CategoryMapper;
import com.fgnb.mbg.po.Category;
import com.fgnb.mbg.po.CategoryExample;
import com.fgnb.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by jiangyitao.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加分类
     *
     * @param category
     * @return
     */
    public Response addCategory(Category category) {
        category.setCreateTime(new Date());
        try {
            int insertRow = categoryMapper.insertSelective(category);
            if (insertRow != 1) {
                return Response.fail("添加分类失败");
            }
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return Response.success("添加成功");
    }

    /**
     * 查询列表
     *
     * @param category
     * @return
     */
    public Response list(Category category) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        if (category.getId() != null) {
            criteria.andIdEqualTo(category.getId());
        }
        if (!StringUtils.isEmpty(category.getName())) {
            criteria.andNameEqualTo(category.getName());
        }
        if (category.getType() != null) {
            criteria.andTypeEqualTo(category.getType());
        }
        if (category.getProjectId() != null) {
            criteria.andProjectIdEqualTo(category.getProjectId());
        }
        categoryExample.setOrderByClause("create_time desc");

        return Response.success(categoryMapper.selectByExample(categoryExample));
    }

}
