package com.blog.blograss.apis.adminuser;

import org.springframework.http.ResponseEntity;

import com.blog.blograss.apis.adminuser.object.AdminUserDto;
import com.blog.blograss.commons.response.Message;

public interface AdminUserService {
    
    ResponseEntity<Message> insertAdminUser(AdminUserDto adminUserDto);

    ResponseEntity<Message> login(AdminUserDto adminUserDto);

    ResponseEntity<Message> logout(String accessToken, String refreshToken);

    ResponseEntity<Message> reissue(String accessToken, String refreshToken);

    ResponseEntity<Message> getUserInfo(String adminId);

    AdminUserDto getAdminUserById(String adminId);

}
