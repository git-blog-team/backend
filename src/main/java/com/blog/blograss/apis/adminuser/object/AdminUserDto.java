package com.blog.blograss.apis.adminuser.object;

import lombok.Data;

@Data
public class AdminUserDto {
    
    private String adminId;
    private String password;
    private String adminName;
    private String secretKey;

}
