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
import com.blog.blograss.commons.util.RedisUtil;

import io.jsonwebtoken.Claims;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ResponseEntity<Message> insertAdminUser(AdminUserDto adminUserDto) {
        try {
            adminUserMapper.insertAdminUser(adminUserDto);
            return ResponseEntity.ok().body(Message.write("SUCCESS"));
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
        AdminUserDto findUserDto = adminUserMapper.getAdminUserById(adminUserDto.getAdminId());

        if(findUserDto == null) {
            return ResponseEntity.badRequest().body(Message.write("EMAIL_NOTFOUND_ERR"));
        }

        if(!passwordEncoder.matches(adminUserDto.getPassword(), findUserDto.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.write("PASSWORD_DISCREPANCY_ERR"));
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDto.getAdminId(), adminUserDto.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto refreshToken = tokenProvider.createRefreshToken(authentication);
        TokenDto accessToken = tokenProvider.createToken(authentication, refreshToken.getRefreshToken());

        findUserDto.setPassword(null);

        accessToken.setRefreshToken(refreshToken.getRefreshToken());
        accessToken.setAdminInfo(findUserDto);

        return ResponseEntity.ok().body(Message.write("SUCESS", accessToken));
    }

    @Override
    public ResponseEntity<Message> logout(String accessToken, String refreshToken) {
        Claims claims = tokenProvider.getClaims(refreshToken);
        String userId = claims.getSubject();

        if (!redisUtil.get(refreshToken).equals(userId))
            return ResponseEntity.badRequest().body(Message.write("USER_NOTFOUND_ERR"));

        try {

            redisUtil.delete(accessToken);
            redisUtil.delete(refreshToken);

            return ResponseEntity.ok().body(Message.write("SUCESS"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.write(e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<Message> reissue(String accessToken, String refreshToken) {
        Claims claims = tokenProvider.getClaims(refreshToken);

        String userId = claims.getSubject();

        Object redisUserId = redisUtil.get(refreshToken);

        if (!userId.equals(redisUserId)) {
            return ResponseEntity.badRequest().body(Message.write("REFRESHTOKEN_NOTFOUND_ERR"));
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null);

        TokenDto tokenDto = tokenProvider.createToken(authentication, refreshToken);

        AdminUserDto adminUserDto = adminUserMapper.getAdminUserById(userId);

        adminUserDto.setPassword(null);

        tokenDto.setAdminInfo(adminUserDto);

        return ResponseEntity.ok().body(Message.write("SUCESS", tokenDto));
    }

    @Override
    public ResponseEntity<Message> getUserInfo(String adminId) {

        try {
            AdminUserDto adminUserDto = adminUserMapper.getAdminUserById(adminId);

            adminUserDto.setPassword(null);
        
            return ResponseEntity.ok().body(Message.write("SUCCESS", adminUserDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("FAIL", e.toString()));
        }
    }
    
}
