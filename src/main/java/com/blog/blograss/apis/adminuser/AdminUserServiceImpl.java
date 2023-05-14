package com.blog.blograss.apis.adminuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.blograss.apis.adminuser.object.AdminUserDto;
import com.blog.blograss.apis.adminuser.object.TokenDto;
import com.blog.blograss.commons.jwt.JwtTokenProvider;
import com.blog.blograss.commons.response.Message;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

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

    @Override
    public ResponseEntity<Message> login(AdminUserDto adminUserDto) {
        AdminUserDto findAdminUSerDto = adminUserMapper.getAdminUserById(adminUserDto.getAdminId());

        if(findAdminUSerDto == null) {
            return ResponseEntity.badRequest().body(Message.write("EMAIL_NOTFOUND_ERR"));
        }

        if(!passwordEncoder.matches(adminUserDto.getPassword(), findAdminUSerDto.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.write("PASSWORD_DISCREPANCY_ERR"));
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDto.getAdminId(), adminUserDto.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto refreshToken = tokenProvider.createRefreshToken(authentication);
        TokenDto accessToken = tokenProvider.createToken(authentication, refreshToken.getRefreshToken());

        accessToken.setRefreshToken(refreshToken.getRefreshToken());

        return ResponseEntity.ok().body(Message.write("SUCESS", accessToken));
    }
    
}
