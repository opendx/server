package com.fgnb.service;

import com.fgnb.mbg.po.PageExample;
import com.fgnb.model.UserCache;
import com.github.pagehelper.PageHelper;
import com.fgnb.mbg.mapper.PageMapper;
import com.fgnb.mbg.po.Page;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.model.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

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

    /**
     * 添加page
     *
     * @param page
     * @return
     */
    public Response add(Page page) {
        page.setCreateTime(new Date());
        page.setCreatorUid(getUid());

        try {
            int insertRow = pageMapper.insertSelective(page);
            if (insertRow != 1) {
                return Response.fail("新增失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        return Response.success("添加成功");
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
        int delRow = pageMapper.deleteByPrimaryKey(pageId);
        if (delRow != 1) {
            return Response.fail("删除失败，请刷新重试");
        }
        return Response.success("删除成功");
    }

    /**
     * 更新page
     *
     * @param page
     */
    public Response update(Page page) {
        if (page.getId() == null) {
            return Response.fail("pageId不能为空");
        }
        try {
            // todo 如果categoryid为空，无法修改page分类
            int updateRow = pageMapper.updateByPrimaryKeySelective(page);
            if (updateRow != 1) {
                return Response.fail("更新失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return Response.success("更新成功");
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
            long total = com.fgnb.model.Page.getTotal(pages);
            return Response.success(com.fgnb.model.Page.build(pageVos,total));
        } else {
            return Response.success(pageVos);
        }
    }

    public List<Page> selectByPage(Page page) {
        if(page == null) {
            page = new Page();
        }

        PageExample pageExample = new PageExample();
        PageExample.Criteria criteria = pageExample.createCriteria();

        if(page.getId() != null) {
            criteria.andIdEqualTo(page.getId());
        }
        if(page.getCategoryId() != null) {
            criteria.andCategoryIdEqualTo(page.getCategoryId());
        }
        if(page.getProjectId() != null) {
            criteria.andProjectIdEqualTo(page.getProjectId());
        }

        pageExample.setOrderByClause("create_time desc");
        return pageMapper.selectByExampleWithBLOBs(pageExample);
    }

}
