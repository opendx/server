package com.daxiang.service;

import com.daxiang.exception.BusinessException;
import com.daxiang.mbg.mapper.CategoryMapper;
import com.daxiang.mbg.po.*;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.dto.CategoryTreeNode;
import com.daxiang.model.vo.CategoryVo;
import com.daxiang.security.SecurityUtil;
import com.daxiang.utils.Tree;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Response delete(Integer categoryId, Integer type, Integer projectId) {
        if (categoryId == null || type == null || projectId == null) {
            return Response.fail("categoryId || type || projectId 不能为空");
        }

        Set<Integer> categoryIds = getDescendantIds(categoryId, type, projectId); // 后代
        categoryIds.add(categoryId);
        List<Integer> cids = new ArrayList<>(categoryIds);

        switch (type) {
            case Category.TYPE_PAGE:
                List<Page> pages = pageService.getPagesWithoutWindowHierarchyByCategoryIds(cids);
                if (!CollectionUtils.isEmpty(pages)) {
                    return Response.fail("分类下有page，无法删除");
                }
                break;
            case Category.TYPE_ACTION: {
                List<Action> actions = actionService.getActionsByCategoryIds(cids);
                if (!CollectionUtils.isEmpty(actions)) {
                    return Response.fail("分类下有action，无法删除");
                }
                break;
            }
            case Category.TYPE_TESTCASE: {
                List<Action> actions = actionService.getActionsByCategoryIds(cids);
                if (!CollectionUtils.isEmpty(actions)) {
                    return Response.fail("分类下有测试用例，无法删除");
                }
                break;
            }
            case Category.TYPE_GLOBAL_VAR:
                List<GlobalVar> globalVars = globalVarService.getGlobalVarsByCategoryIds(cids);
                if (!CollectionUtils.isEmpty(globalVars)) {
                    return Response.fail("分类下有全局变量，无法删除");
                }
                break;
            default:
                return Response.fail("不支持的category type");
        }

        CategoryExample example = new CategoryExample();
        CategoryExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(cids);

        int deleteRow = categoryMapper.deleteByExample(example);
        if (deleteRow == categoryIds.size()) {
            return Response.success("删除成功");
        } else {
            throw new BusinessException("删除失败，请稍后重试");
        }
    }

    public Response update(Category category) {
        int updateRow;
        try {
            updateRow = categoryMapper.updateByPrimaryKeySelective(category);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return updateRow == 1 ? Response.success("保存成功") : Response.fail("保存失败，请稍后重试");
    }

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

    public Response getCategoryTreeByProjectIdAndType(Integer projectId, Integer type) {
        if (projectId == null || type == null) {
            return Response.fail("projectId或type不能为空");
        }

        Category query = new Category();
        query.setProjectId(projectId);
        query.setType(type);
        List<Category> categories = selectByCategory(query);

        return Response.success(buildCategoryTree(categories));
    }

    private List<Tree.TreeNode> buildCategoryTree(List<Category> categories) {
        List<CategoryTreeNode> categroyTreeNodes = categories.stream()
                .map(CategoryTreeNode::create).collect(Collectors.toList());

        return Tree.build(categroyTreeNodes);
    }

    private List<CategoryVo> convertCategoriesToCategoryVos(List<Category> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return new ArrayList<>();
        }

        List<Integer> creatorUids = categories.stream()
                .map(Category::getCreatorUid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByIds(creatorUids);

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

    private List<Category> selectByCategory(Category category) {
        CategoryExample example = new CategoryExample();
        CategoryExample.Criteria criteria = example.createCriteria();

        if (category != null) {
            if (category.getId() != null) {
                criteria.andIdEqualTo(category.getId());
            }
            if (category.getParentId() != null) {
                criteria.andParentIdEqualTo(category.getParentId());
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
        }

        return categoryMapper.selectByExample(example);
    }

    public List<Category> getCategoriesWithProjectIdIsNullOrProjectIdEqualsTo(Integer projectId) {
        if (projectId == null) {
            return new ArrayList<>();
        }

        CategoryExample example = new CategoryExample();

        CategoryExample.Criteria c1 = example.createCriteria();
        c1.andProjectIdIsNull();

        CategoryExample.Criteria c2 = example.createCriteria();
        c2.andProjectIdEqualTo(projectId);

        example.or(c2);
        return categoryMapper.selectByExample(example);
    }

    /**
     * 获取后代ids
     *
     * @param parentId
     * @param type
     * @param projectId
     * @return
     */
    private Set<Integer> getDescendantIds(Integer parentId, Integer type, Integer projectId) {
        Category query = new Category();
        query.setProjectId(projectId);
        query.setType(type);
        List<Category> categories = selectByCategory(query);
        Map<Integer, Category> categoryMap = categoriesToMap(categories);

        Set<Integer> descendantIds = new HashSet<>();

        for (Category category : categories) {
            Integer pid = category.getParentId();
            while (pid != null && pid > 0) {
                if (parentId.equals(pid)) {
                    descendantIds.add(category.getId());
                    break;
                } else {
                    if (categoryMap.containsKey(pid)) {
                        pid = categoryMap.get(pid).getParentId(); // 往上取
                    } else {
                        break;
                    }
                }
            }
        }

        return descendantIds;
    }

    public Map<Integer, Category> categoriesToMap(List<Category> categories) {
        return categories.stream()
                .collect(Collectors.toMap(Category::getId, c -> c, (k1, k2) -> k1));
    }

}
