package com.blog.blograss.apis.adminuser.object;

import lombok.Builder;
import lombok.Data;

@Data
public class TokenDto {

    private String accessToken;
    private String refreshToken;
    private AdminUserDto adminInfo;

    @Builder
    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}