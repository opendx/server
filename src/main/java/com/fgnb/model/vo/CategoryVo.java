package com.fgnb.model.vo;

import com.fgnb.mbg.po.Category;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class CategoryVo extends Category {
    private String creatorNickName;

    public static CategoryVo convert(Category category, String creatorNickName) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        categoryVo.setCreatorNickName(creatorNickName);
        return categoryVo;
    }
}
