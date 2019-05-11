package com.fgnb.dao;

import com.fgnb.mbg.po.Page;
import com.fgnb.model.vo.PageVo;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface PageDao {
    List<PageVo> selectByPage(Page page);
}
