package com.daxiang.dao;

import com.daxiang.mbg.po.Page;
import com.daxiang.mbg.po.PageExample;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface PageDao {
    List<Page> selectPagesWithoutWindowHierarchyByExample(PageExample example);
}
