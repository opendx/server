package com.yqhp.dao;

import com.yqhp.mbg.po.Page;
import com.yqhp.model.vo.PageVo;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface PageDao {
    List<PageVo> selectByPage(Page page);
}
