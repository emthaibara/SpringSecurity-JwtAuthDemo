package com.securityserviceprovider.dao;

import com.securityserviceprovider.entity.AuthUser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AuthUserMapper {
    int deleteByPrimaryKey(String phonenumber);

    int insert(AuthUser record);

    AuthUser selectByPrimaryKey(String phonenumber);

    List<AuthUser> selectAll();

    int updateByPrimaryKey(AuthUser record);
}