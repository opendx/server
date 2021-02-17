package com.daxiang.service;

import com.daxiang.dao.PageDao;
import com.daxiang.exception.ServerException;
import com.daxiang.mbg.po.*;
import com.daxiang.model.PagedData;
import com.daxiang.model.enums.UploadDir;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.daxiang.mbg.mapper.PageMapper;
import com.daxiang.model.PageRequest;
import com.daxiang.model.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Slf4j
@Service
public class PageService {

    @Autowired
    private PageMapper pageMapper;
    @Autowired
    private PageDao pageDao;
    @Autowired
    private ActionService actionService;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @Transactional
    public void add(Page page) {
        page.setCreateTime(new Date());
        page.setCreatorUid(SecurityUtil.getCurrentUserId());

        String originalImgPath = page.getImgPath();
        String destImgPath = null;
        if (!StringUtils.isEmpty(originalImgPath)) {
            destImgPath = UploadDir.IMG.path + "/" + FilenameUtils.getName(originalImgPath);
            page.setImgPath(destImgPath);
        }

        try {
            int insertCount = pageMapper.insertSelective(page);
            if (insertCount != 1) {
                throw new ServerException("添加失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(page.getName() + "已存在");
        }

        // 数据库添加成功，图片移动到IMG目录
        if (!StringUtils.isEmpty(destImgPath)) {
            try {
                fileService.moveFile(originalImgPath, destImgPath);
            } catch (FileNotFoundException e) {
                log.info("file not found: {}", originalImgPath);
                throw new ServerException("图片不存在，该图片可能已保存至其他Page");
            } catch (IOException e) {
                log.error("move {} -> {} err", originalImgPath, destImgPath, e);
                throw new ServerException(e.getMessage());
            }
        }
    }

    public void deleteAndClearRelatedRes(Integer pageId) {
        Page page = pageMapper.selectByPrimaryKey(pageId);
        if (page == null) {
            throw new ServerException("page不存在");
        }

        // 检查Page名下是否有action
        List<Action> actions = actionService.getActionsByPageId(pageId);
        if (!CollectionUtils.isEmpty(actions)) {
            String actionNames = actions.stream().map(Action::getName).collect(Collectors.joining("、"));
            throw new ServerException("actions: " + actionNames + "，已绑定该page，无法删除");
        }

        int deleteCount = pageMapper.deleteByPrimaryKey(pageId);
        if (deleteCount != 1) {
            throw new ServerException("删除失败，请稍后重试");
        }

        fileService.deleteQuietly(page.getImgPath());
    }

    public void update(Page page) {
        try {
            int updateCount = pageMapper.updateByPrimaryKeyWithBLOBs(page);
            if (updateCount != 1) {
                throw new ServerException("更新失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(page.getName() + "已存在");
        }
    }

    public PagedData<PageVo> listWithoutWindowHierarchy(Page query, String orderBy, PageRequest pageRequest) {
        com.github.pagehelper.Page<Object> page = PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "create_time desc";
        }

        List<PageVo> pageVos = getPageVosWithoutWindowHierarchy(query, orderBy);
        return new PagedData<>(pageVos, page.getTotal());
    }

    public PageVo getPageVoById(Integer pageId) {
        if (pageId == null) {
            throw new ServerException("pageId不能为空");
        }

        Page page = pageMapper.selectByPrimaryKey(pageId);
        if (page == null) {
            throw new ServerException("page不存在");
        }

        List<PageVo> pageVos = convertPagesToPageVos(Arrays.asList(page));
        return pageVos.get(0);
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

        List<PageVo> pageVos = pages.stream().map(page -> {
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

        return pageVos;
    }

    public List<PageVo> getPageVosWithoutWindowHierarchy(Page query, String orderBy) {
        List<Page> pages = getPagesWithoutWindowHierarchy(query, orderBy);
        return convertPagesToPageVos(pages);
    }

    public List<Page> getPagesWithoutWindowHierarchy(Page query) {
        return getPagesWithoutWindowHierarchy(query, null);
    }

    public List<Page> getPagesWithoutWindowHierarchy(Page query, String orderBy) {
        PageExample example = new PageExample();

        if (query != null) {
            PageExample.Criteria criteria = example.createCriteria();

            if (query.getId() != null) {
                criteria.andIdEqualTo(query.getId());
            }
            if (query.getCategoryId() != null) {
                criteria.andCategoryIdEqualTo(query.getCategoryId());
            }
            if (query.getProjectId() != null) {
                criteria.andProjectIdEqualTo(query.getProjectId());
            }
        }

        if (!StringUtils.isEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        return pageDao.selectPagesWithoutWindowHierarchyByExample(example);
    }

    public List<Page> getPagesWithoutWindowHierarchyByProjectId(Integer projectId) {
        if (projectId == null) {
            throw new ServerException("projectId不能为空");
        }

        Page query = new Page();
        query.setProjectId(projectId);
        return getPagesWithoutWindowHierarchy(query);
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
