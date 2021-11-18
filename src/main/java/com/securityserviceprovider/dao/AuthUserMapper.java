package com.securityserviceprovider.dao;

import com.securityserviceprovider.pojo.AuthUser;
import java.util.List;

public interface AuthUserMapper {
    int deleteByPrimaryKey(String phonenumber);

    int insert(AuthUser record);

    AuthUser selectByPrimaryKey(String phonenumber);

    List<AuthUser> selectAll();

    int updateByPrimaryKey(AuthUser record);
}