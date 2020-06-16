package com.daxiang.service;

import com.daxiang.dao.PageDao;
import com.daxiang.mbg.po.*;
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
    private PageDao pageDao;
    @Autowired
    private ActionService actionService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    public Response add(Page page) {
        page.setCreateTime(new Date());
        page.setCreatorUid(SecurityUtil.getCurrentUserId());

        int insertRow;
        try {
            insertRow = pageMapper.insertSelective(page);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return insertRow == 1 ? Response.success("添加Page成功", page) : Response.fail("添加Page失败，请稍后重试");
    }

    public Response delete(Integer pageId) {
        if (pageId == null) {
            return Response.fail("pageId不能为空");
        }

        // 检查Page名下是否有action
        List<Action> actions = actionService.getActionsByPageId(pageId);
        if (!CollectionUtils.isEmpty(actions)) {
            String actionNames = actions.stream().map(Action::getName).collect(Collectors.joining("、"));
            return Response.fail("actions: " + actionNames + "，已绑定该page，无法删除");
        }

        int delRow = pageMapper.deleteByPrimaryKey(pageId);
        return delRow == 1 ? Response.success("删除Page成功") : Response.fail("删除Page失败，请稍后重试");
    }

    public Response update(Page page) {
        int updateRow;
        try {
            updateRow = pageMapper.updateByPrimaryKeyWithBLOBs(page);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return updateRow == 1 ? Response.success("保存Page成功") : Response.fail("保存Page失败，请稍后重试");
    }

    public Response list(Page page, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<Page> pages = selectPagesWithoutWindowHierarchyByPage(page);
        List<PageVo> pageVos = convertPagesToPageVos(pages);

        if (needPaging) {
            long total = com.daxiang.model.Page.getTotal(pages);
            return Response.success(com.daxiang.model.Page.build(pageVos, total));
        } else {
            return Response.success(pageVos);
        }
    }

    public Response getPageVoById(Integer pageId) {
        if (pageId == null) {
            return Response.fail("pageId不能为空");
        }

        Page page = pageMapper.selectByPrimaryKey(pageId);
        if (page == null) {
            return Response.fail("page不存在");
        }

        List<PageVo> pageVos = convertPagesToPageVos(Arrays.asList(page));
        return Response.success(pageVos.get(0));
    }

    private List<PageVo> convertPagesToPageVos(List<Page> pages) {
        if (CollectionUtils.isEmpty(pages)) {
            return new ArrayList<>();
        }

        List<Integer> creatorUids = pages.stream()
                .map(Page::getCreatorUid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByIds(creatorUids);

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

    private List<Page> selectPagesWithoutWindowHierarchyByPage(Page page) {
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

        return pageDao.selectPagesWithoutWindowHierarchyByExample(example);
    }

    public List<Page> getPagesWithoutWindowHierarchyByProjectId(Integer projectId) {
        if (projectId == null) {
            return new ArrayList<>();
        }

        Page query = new Page();
        query.setProjectId(projectId);
        return selectPagesWithoutWindowHierarchyByPage(query);
    }

    public List<Page> getPagesWithoutWindowHierarchyByCategoryIds(List<Integer> categoryIds) {
        if (CollectionUtils.isEmpty(categoryIds)) {
            return new ArrayList<>();
        }

        PageExample example = new PageExample();
        PageExample.Criteria criteria = example.createCriteria();

        criteria.andCategoryIdIn(categoryIds);
        return pageDao.selectPagesWithoutWindowHierarchyByExample(example);
    }

}
