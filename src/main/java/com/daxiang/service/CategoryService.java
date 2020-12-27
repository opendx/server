package com.daxiang.service;

import com.daxiang.exception.ServerException;
import com.daxiang.mbg.mapper.CategoryMapper;
import com.daxiang.mbg.po.*;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.dto.CategoryTreeNode;
import com.daxiang.model.vo.CategoryVo;
import com.daxiang.security.SecurityUtil;
import com.daxiang.utils.Tree;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
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

    public void add(Category category) {
        category.setCreateTime(new Date());
        category.setCreatorUid(SecurityUtil.getCurrentUserId());

        try {
            int insertCount = categoryMapper.insertSelective(category);
            if (insertCount != 1) {
                throw new ServerException("添加失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(category.getName() + "已存在");
        }
    }

    @Transactional
    public void delete(Integer categoryId, Integer type, Integer projectId) {
        if (categoryId == null || type == null || projectId == null) {
            throw new ServerException("categoryId or type or projectId不能为空");
        }

        Set<Integer> categoryIds = getDescendantIds(categoryId, type, projectId); // 后代
        categoryIds.add(categoryId); // 后代和自己
        List<Integer> cids = new ArrayList<>(categoryIds);

        switch (type) {
            case Category.TYPE_PAGE:
                List<com.daxiang.mbg.po.Page> pages = pageService.getPagesWithoutWindowHierarchyByCategoryIds(cids);
                if (!CollectionUtils.isEmpty(pages)) {
                    throw new ServerException("分类下有page，无法删除");
                }
                break;
            case Category.TYPE_ACTION: {
                List<Action> actions = actionService.getActionsByCategoryIds(cids);
                if (!CollectionUtils.isEmpty(actions)) {
                    throw new ServerException("分类下有action，无法删除");
                }
                break;
            }
            case Category.TYPE_TESTCASE: {
                List<Action> actions = actionService.getActionsByCategoryIds(cids);
                if (!CollectionUtils.isEmpty(actions)) {
                    throw new ServerException("分类下有测试用例，无法删除");
                }
                break;
            }
            case Category.TYPE_GLOBAL_VAR:
                List<GlobalVar> globalVars = globalVarService.getGlobalVarsByCategoryIds(cids);
                if (!CollectionUtils.isEmpty(globalVars)) {
                    throw new ServerException("分类下有全局变量，无法删除");
                }
                break;
            default:
                throw new ServerException("不支持的category type");
        }

        CategoryExample example = new CategoryExample();
        CategoryExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(cids);

        int deleteCount = categoryMapper.deleteByExample(example);
        if (deleteCount != cids.size()) {
            throw new ServerException("删除失败，请稍后重试");
        }
    }

    public void update(Category category) {
        try {
            int updateCount = categoryMapper.updateByPrimaryKeySelective(category);
            if (updateCount != 1) {
                throw new ServerException("更新失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(category.getName() + "已存在");
        }
    }

    public PagedData<CategoryVo> list(Category query, String orderBy, PageRequest pageRequest) {
        Page page = PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        List<CategoryVo> categoryVos = getCategoryVos(query, orderBy);
        return new PagedData<>(categoryVos, page.getTotal());
    }

    public List<Tree.TreeNode> getCategoryTreeByProjectIdAndType(Integer projectId, Integer type) {
        if (projectId == null || type == null) {
            throw new ServerException("projectId or type不能为空");
        }

        Category query = new Category();
        query.setProjectId(projectId);
        query.setType(type);
        List<Category> categories = getCategories(query);

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

        List<CategoryVo> categoryVos = categories.stream().map(category -> {
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

        return categoryVos;
    }

    public List<CategoryVo> getCategoryVos(Category query, String orderBy) {
        List<Category> categories = getCategories(query, orderBy);
        return convertCategoriesToCategoryVos(categories);
    }

    public List<Category> getCategories(Category query) {
        return getCategories(query, null);
    }

    public List<Category> getCategories(Category query, String orderBy) {
        CategoryExample example = new CategoryExample();

        if (query != null) {
            CategoryExample.Criteria criteria = example.createCriteria();

            if (query.getId() != null) {
                criteria.andIdEqualTo(query.getId());
            }
            if (query.getParentId() != null) {
                criteria.andParentIdEqualTo(query.getParentId());
            }
            if (!StringUtils.isEmpty(query.getName())) {
                criteria.andNameEqualTo(query.getName());
            }
            if (query.getType() != null) {
                criteria.andTypeEqualTo(query.getType());
            }
            if (query.getProjectId() != null) {
                criteria.andProjectIdEqualTo(query.getProjectId());
            }
        }

        if (!StringUtils.isEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        return categoryMapper.selectByExample(example);
    }

    public List<Category> getCategoriesWithProjectIdIsNullOrProjectIdEqualsTo(Integer projectId) {
        if (projectId == null) {
            throw new ServerException("projectId不能为空");
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
        List<Category> categories = getCategories(query);
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
                .collect(Collectors.toMap(Category::getId, Function.identity(), (k1, k2) -> k1));
    }

}
