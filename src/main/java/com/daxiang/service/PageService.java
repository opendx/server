package com.daxiang.service;

import com.daxiang.mbg.po.*;
import com.daxiang.model.vo.PageCascaderVo;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.daxiang.mbg.mapper.PageMapper;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
public class PageService {

    @Autowired
    private PageMapper pageMapper;
    @Autowired
    private ActionService actionService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    /**
     * 添加page
     *
     * @param page
     * @return
     */
    public Response add(Page page) {
        page.setCreateTime(new Date());
        page.setCreatorUid(SecurityUtil.getCurrentUserId());

        int insertRow;
        try {
            insertRow = pageMapper.insertSelective(page);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return insertRow == 1 ? Response.success("添加Page成功") : Response.fail("添加Page失败，请稍后重试");
    }

    /**
     * 删除page
     *
     * @param pageId
     */
    public Response delete(Integer pageId) {
        if (pageId == null) {
            return Response.fail("pageId不能为空");
        }

        // 检查Page名下是否有action
        Action query = new Action();
        query.setPageId(pageId);
        List<Action> actions = actionService.selectByAction(query);
        if (!CollectionUtils.isEmpty(actions)) {
            return Response.fail("有action绑定了该page，无法删除");
        }

        int delRow = pageMapper.deleteByPrimaryKey(pageId);
        return delRow == 1 ? Response.success("删除Page成功") : Response.fail("删除Page失败，请稍后重试");
    }

    /**
     * 更新page
     *
     * @param page
     */
    public Response update(Page page) {
        int updateRow;
        try {
            updateRow = pageMapper.updateByPrimaryKeyWithBLOBs(page);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return updateRow == 1 ? Response.success("更新Page成功") : Response.fail("更新Page失败，请稍后重试");
    }

    /**
     * 查询page列表
     *
     * @param page
     * @param pageRequest
     * @return
     */
    public Response list(Page page, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<Page> pages = selectByPage(page);
        List<PageVo> pageVos = convertPagesToPageVos(pages);

        if (needPaging) {
            long total = com.daxiang.model.Page.getTotal(pages);
            return Response.success(com.daxiang.model.Page.build(pageVos, total));
        } else {
            return Response.success(pageVos);
        }
    }

    private List<PageVo> convertPagesToPageVos(List<Page> pages) {
        if (CollectionUtils.isEmpty(pages)) {
            return Collections.EMPTY_LIST;
        }

        List<Integer> creatorUids = pages.stream()
                .map(Page::getCreatorUid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByUserIds(creatorUids);

        return pages.stream().map(page -> {
            PageVo pageVo = new PageVo();
            BeanUtils.copyProperties(page, pageVo);

            if (page.getCreatorUid() != null) {
                User user = userMap.get(page.getCreatorUid());
                if (user != null) {
                    pageVo.setCreatorNickName(user.getNickName());
                }
            }

            return pageVo;
        }).collect(Collectors.toList());
    }

    public List<Page> selectByPage(Page page) {
        PageExample example = new PageExample();
        PageExample.Criteria criteria = example.createCriteria();

        if (page != null) {
            if (page.getId() != null) {
                criteria.andIdEqualTo(page.getId());
            }
            if (page.getCategoryId() != null) {
                criteria.andCategoryIdEqualTo(page.getCategoryId());
            }
            if (page.getProjectId() != null) {
                criteria.andProjectIdEqualTo(page.getProjectId());
            }
        }
        example.setOrderByClause("create_time desc");

        return pageMapper.selectByExampleWithBLOBs(example);
    }

    public List<Page> findByProjectId(Integer projectId) {
        Assert.notNull(projectId, "projectId cannot be null");

        Page query = new Page();
        query.setProjectId(projectId);
        return selectByPage(query);
    }

    public Response cascader(Integer projectId) {
        if (projectId == null) {
            return Response.fail("projectId不能为空");
        }

        Page query = new Page();
        query.setProjectId(projectId);
        List<Page> pages = selectByPage(query);

        List<Integer> categoryIds = pages.stream()
                .map(Page::getCategoryId)
                .filter(Objects::nonNull)
                .distinct().collect(Collectors.toList());
        List<Category> categories = categoryService.selectByPrimaryKeys(categoryIds);

        // 带分类的page
        List<PageCascaderVo> result = categories.stream().map(category -> {
            PageCascaderVo root = new PageCascaderVo();
            root.setName(category.getName());

            List<PageCascaderVo> children = pages.stream()
                    .filter(p -> category.getId().equals(p.getCategoryId()))
                    .map(p -> PageCascaderVo.convert(p)).collect(Collectors.toList());
            root.setChildren(children);

            return root;
        }).collect(Collectors.toList());

        // 不带分类的page
        List<PageCascaderVo> pageCascaderVosWithoutCategory = pages.stream()
                .filter(p -> Objects.isNull(p.getCategoryId()))
                .map(p -> PageCascaderVo.convert(p)).collect(Collectors.toList());

        result.addAll(pageCascaderVosWithoutCategory);

        return Response.success(result);
    }
}
