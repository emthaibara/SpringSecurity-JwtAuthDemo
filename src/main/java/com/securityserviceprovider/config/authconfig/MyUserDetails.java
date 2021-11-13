package com.securityserviceprovider.config.authconfig;

import com.securityserviceprovider.dao.AuthUserMapper;
import com.securityserviceprovider.entity.AuthUser;
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
public class MyUserDetails implements UserDetailsService {

    private String phoneNumber;

    private String password;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthUser authUser;

    @Resource
    private AuthUserMapper authUserMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        authUser = authUserMapper.selectByPrimaryKey(s);
        return User.builder().
                username(s).
                password(passwordEncoder.encode(authUser.getPassword())).
                roles(authUser.getRole()).
                build();
    }

    @Override
    public String toString() {
        return "MyUserDetails{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


}
