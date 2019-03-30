package com.yqhp.model.vo;

import com.yqhp.mbg.po.User;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class UserVo extends User {
    private String token;
}
