package com.daxiang.service;

import com.daxiang.mbg.mapper.CategoryMapper;
import com.daxiang.mbg.po.*;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.vo.CategoryVo;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private PageService pageService;
    @Autowired
    private ActionService actionService;
    @Autowired
    private GlobalVarService globalVarService;
    @Autowired
    private UserService userService;

    /**
     * 添加分类
     *
     * @param category
     * @return
     */
    public Response add(Category category) {
        category.setCreateTime(new Date());
        category.setCreatorUid(SecurityUtil.getCurrentUserId());

        int insertRow;
        try {
            insertRow = categoryMapper.insertSelective(category);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return insertRow == 1 ? Response.success("添加Category成功") : Response.fail("添加Category失败");
    }

    public Response delete(Integer categoryId) {
        if (categoryId == null) {
            return Response.fail("categoryId不能为空");
        }

        Category categoryQuery = new Category();
        categoryQuery.setId(categoryId);
        List<Category> categories = selectByCategory(categoryQuery);
        if (CollectionUtils.isEmpty(categories)) {
            return Response.fail("分类不存在");
        }

        Category category = categories.get(0);

        if (category.getType() == Category.TYPE_PAGE) {
            // 检查该分类下是否有page
            Page query = new Page();
            query.setCategoryId(categoryId);
            List<Page> pages = pageService.selectByPageWithoutWindowHierarchy(query);
            if (!CollectionUtils.isEmpty(pages)) {
                return Response.fail("分类下有page，无法删除");
            }
        } else if (category.getType() == Category.TYPE_ACTION || category.getType() == Category.TYPE_TESTCASE) {
            // 检查该分类下是否有action
            Action query = new Action();
            query.setCategoryId(categoryId);
            List<Action> actions = actionService.selectByAction(query);
            if (!CollectionUtils.isEmpty(actions)) {
                return Response.fail("分类下有action，无法删除");
            }
        } else if (category.getType() == Category.TYPE_GLOBAL_VAR) {
            // 检查该分类下是否有globalVar
            GlobalVar query = new GlobalVar();
            query.setCategoryId(categoryId);
            List<GlobalVar> globalVars = globalVarService.selectByGlobalVar(query);
            if (!CollectionUtils.isEmpty(globalVars)) {
                return Response.fail("分类下有全局变量，无法删除");
            }
        } else {
            return Response.fail("不支持的分类类型");
        }

        int deleteRow = categoryMapper.deleteByPrimaryKey(categoryId);
        return deleteRow == 1 ? Response.success("删除分类成功") : Response.fail("删除分类失败，请稍后重试");
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
        List<CategoryVo> categoryVos = convertCategoriesToCategoryVos(categories);

        if (needPaging) {
            long total = com.daxiang.model.Page.getTotal(categories);
            return Response.success(com.daxiang.model.Page.build(categoryVos, total));
        } else {
            return Response.success(categoryVos);
        }
    }

    private List<CategoryVo> convertCategoriesToCategoryVos(List<Category> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.EMPTY_LIST;
        }

        List<Integer> creatorUids = categories.stream()
                .map(Category::getCreatorUid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByUserIds(creatorUids);

        return categories.stream().map(category -> {
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category, categoryVo);

            if (category.getCreatorUid() != null) {
                User user = userMap.get(category.getCreatorUid());
                if (user != null) {
                    categoryVo.setCreatorNickName(user.getNickName());
                }
            }

            return categoryVo;
        }).collect(Collectors.toList());
    }

    public List<Category> selectByCategory(Category category) {
        CategoryExample example = new CategoryExample();
        CategoryExample.Criteria criteria = example.createCriteria();

        if (category != null) {
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
        }

        return categoryMapper.selectByExample(example);
    }

    public List<Category> selectByPrimaryKeys(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }

        CategoryExample example = new CategoryExample();
        CategoryExample.Criteria criteria = example.createCriteria();

        criteria.andIdIn(ids);
        return categoryMapper.selectByExample(example);
    }

    public Map<Integer, Category> getCategoryMapByCategoryIds(List<Integer> ids) {
        List<Category> categories = selectByPrimaryKeys(ids);
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.EMPTY_MAP;
        }

        return categories.stream().collect(Collectors.toMap(Category::getId, c -> c, (k1, k2) -> k1));
    }

}
