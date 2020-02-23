package com.daxiang.model.vo;

import com.daxiang.mbg.po.Category;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class CategoryVo extends Category {
    private String creatorNickName = "";
}
