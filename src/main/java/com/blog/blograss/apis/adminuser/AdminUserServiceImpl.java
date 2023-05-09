package com.blog.blograss.apis.adminuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.blog.blograss.apis.adminuser.object.AdminUserDto;
import com.blog.blograss.commons.response.Message;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public ResponseEntity<Message> insertAdminUser(AdminUserDto adminUserDto) {
        try {
            adminUserMapper.insertAdminUser(adminUserDto);
            return ResponseEntity.ok().body(Message.write("SUCESS"));
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }

    @Override
    public AdminUserDto getAdminUserById(String adminId) {
        return adminUserMapper.getAdminUserById(adminId);
    }
    
}
