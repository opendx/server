package com.daxiang.security;

import com.daxiang.model.dto.UserDto;
import com.daxiang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = userService.getUserDtoByUsername(username);
        if (userDto == null) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return userDto;
    }

}
