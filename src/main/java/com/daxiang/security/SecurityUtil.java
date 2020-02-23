package com.daxiang.security;

import com.daxiang.model.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by jiangyitao.
 */
public class SecurityUtil {

    public static Integer getCurrentUserId() {
        return getCurrentUserDto().getId();
    }

    public static UserDto getCurrentUserDto() {
        return (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
