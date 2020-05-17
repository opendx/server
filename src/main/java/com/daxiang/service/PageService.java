package com.daxiang.service;

import com.daxiang.dao.PageDao;
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
    private PageDao pageDao;
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
        return insertRow == 1 ? Response.success("添加Page成功", page) : Response.fail("添加Page失败，请稍后重试");
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
        return updateRow == 1 ? Response.success("保存Page成功") : Response.fail("保存Page失败，请稍后重试");
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

        List<Page> pages = selectByPageWithoutWindowHierarchy(page);
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

    public List<Page> selectByPageWithoutWindowHierarchy(Page page) {
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

        return pageDao.selectByExampleWithoutWindowHierarchy(example);
    }

    public List<Page> findByProjectIdWithoutWindowHierarchy(Integer projectId) {
        Assert.notNull(projectId, "projectId cannot be null");

        Page query = new Page();
        query.setProjectId(projectId);
        return selectByPageWithoutWindowHierarchy(query);
    }

    public Response cascader(Integer projectId) {
        if (projectId == null) {
            return Response.fail("projectId不能为空");
        }

        Page query = new Page();
        query.setProjectId(projectId);
        List<Page> pages = selectByPageWithoutWindowHierarchy(query);

        List<Integer> categoryIds = pages.stream()
                .map(Page::getCategoryId)
                .filter(Objects::nonNull)
                .distinct().collect(Collectors.toList());
        Map<Integer, Category> categoryMap = categoryService.getCategoryMapByCategoryIds(categoryIds);

        // 按照是否有分类分区
        Map<Boolean, List<Page>> pagesMap = pages.stream()
                .collect(Collectors.partitioningBy(page -> Objects.nonNull(page.getCategoryId())));

        List<PageCascaderVo> result = new ArrayList<>();

        // 有分类的pages 按照分类分组
        Map<Integer, List<Page>> pagesWithCategoryMap = pagesMap.get(true).stream()
                .collect(Collectors.groupingBy(Page::getCategoryId));

        pagesWithCategoryMap.forEach((categoryId, pagesWithCategory) -> {
            PageCascaderVo root = new PageCascaderVo();
            root.setName(categoryMap.get(categoryId).getName());

            List<PageCascaderVo> children = pagesWithCategory.stream()
                    .map(PageCascaderVo::convert).collect(Collectors.toList());
            root.setChildren(children);

            result.add(root);
        });

        // 无分类的pages
        List<PageCascaderVo> pageWithoutCategoryCascaderVos = pagesMap.get(false).stream()
                .map(PageCascaderVo::convert).collect(Collectors.toList());

        result.addAll(pageWithoutCategoryCascaderVos);
        return Response.success(result);
    }

    public Response findById(Integer pageId) {
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
}
