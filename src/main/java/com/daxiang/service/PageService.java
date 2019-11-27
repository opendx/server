package com.daxiang.service;

import com.daxiang.mbg.po.Action;
import com.daxiang.mbg.po.PageExample;
import com.daxiang.model.UserCache;
import com.github.pagehelper.PageHelper;
import com.daxiang.mbg.mapper.PageMapper;
import com.daxiang.mbg.po.Page;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
public class PageService extends BaseService {

    @Autowired
    private PageMapper pageMapper;
    @Autowired
    private ActionService actionService;

    /**
     * 添加page
     *
     * @param page
     * @return
     */
    public Response add(Page page) {
        page.setCreateTime(new Date());
        page.setCreatorUid(getUid());

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
        List<PageVo> pageVos = pages.stream().map(p -> PageVo.convert(p, UserCache.getNickNameById(p.getCreatorUid()))).collect(Collectors.toList());

        if (needPaging) {
            // java8 stream会导致PageHelper total计算错误
            // 所以这里用pages计算total
            long total = com.daxiang.model.Page.getTotal(pages);
            return Response.success(com.daxiang.model.Page.build(pageVos, total));
        } else {
            return Response.success(pageVos);
        }
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

}
