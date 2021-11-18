package com.securityserviceprovider.config.authconfig;

import com.securityserviceprovider.dao.AuthUserMapper;
import com.securityserviceprovider.pojo.AuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/1
 */

@Component
public class LoginUserDetails implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(LoginUserDetails.class);

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthUserMapper authUserMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AuthUser authUser = authUserMapper.selectByPrimaryKey(s);
        log.info(authUser.toString());
        return User.builder()
                .username(s)
                .password(passwordEncoder.encode(authUser.getPassword()))
                .roles(authUser.getRole())
                .build();
    }
}
