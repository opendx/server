package com.fgnb.model.vo;

import com.fgnb.mbg.po.User;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class UserVo extends User {
    private String token;
}
