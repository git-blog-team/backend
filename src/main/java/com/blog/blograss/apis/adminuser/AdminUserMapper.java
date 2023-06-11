package com.blog.blograss.apis.adminuser;

import org.apache.ibatis.annotations.Mapper;

import com.blog.blograss.apis.adminuser.object.AdminUserDto;

@Mapper
public interface AdminUserMapper {
    
    public void insertAdminUser(AdminUserDto adminUserDto);

    public AdminUserDto getAdminUserById(String adminId);
    
    public AdminUserDto getUserInfo(String adminId);
}
