package com.fgnb.service;

import com.fgnb.mbg.mapper.CategoryMapper;
import com.fgnb.mbg.po.Category;
import com.fgnb.mbg.po.CategoryExample;
import com.fgnb.model.Page;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.model.UserCache;
import com.fgnb.model.vo.CategoryVo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
public class CategoryService extends BaseService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private PageService pageService;

    /**
     * 添加分类
     *
     * @param category
     * @return
     */
    public Response add(Category category) {
        category.setCreateTime(new Date());
        category.setCreatorUid(getUid());

        int insertRow;
        try {
            insertRow = categoryMapper.insertSelective(category);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        if (insertRow == 1) {
            return Response.success("添加Category成功");
        } else {
            return Response.fail("添加Category失败");
        }
    }

    public Response delete(Integer categoryId) {
        if (categoryId == null) {
            return Response.fail("categoryId不能为空");
        }

        // 检查该分类下是否有page
        com.fgnb.mbg.po.Page query = new com.fgnb.mbg.po.Page();
        query.setCategoryId(categoryId);
        List<com.fgnb.mbg.po.Page> pages = pageService.selectByPage(query);
        if (!CollectionUtils.isEmpty(pages)) {
            return Response.fail("分类下有page，无法删除");
        }

        int deleteRow = categoryMapper.deleteByPrimaryKey(categoryId);

        if (deleteRow == 1) {
            return Response.success("删除分类成功");
        } else {
            return Response.fail("删除分类失败，请稍后重试");
        }
    }

    /**
     * 查询列表
     *
     * @return
     */
    public Response list(Category category, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<Category> categories = selectByCategory(category);
        List<CategoryVo> categoryVos = categories.stream().map(c -> CategoryVo.convert(c, UserCache.getNickNameById(c.getCreatorUid()))).collect(Collectors.toList());

        if (needPaging) {
            // java8 stream会导致PageHelper total计算错误
            // 所以这里用categories计算total
            long total = Page.getTotal(categories);
            return Response.success(Page.build(categoryVos, total));
        } else {
            return Response.success(categoryVos);
        }
    }

    public List<Category> selectByCategory(Category category) {
        if (category == null) {
            category = new Category();
        }

        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        if (category.getId() != null) {
            criteria.andIdEqualTo(category.getId());
        }
        if (category.getProjectId() != null) {
            criteria.andProjectIdEqualTo(category.getProjectId());
        }
        if (category.getType() != null) {
            criteria.andTypeEqualTo(category.getType());
        }
        if (!StringUtils.isEmpty(category.getName())) {
            criteria.andNameEqualTo(category.getName());
        }
        categoryExample.setOrderByClause("create_time desc");

        return categoryMapper.selectByExample(categoryExample);
    }

}
