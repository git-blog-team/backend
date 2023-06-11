package com.blog.blograss.apis.adminuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blograss.apis.adminuser.object.AdminUserDto;
import com.blog.blograss.commons.jwt.JwtTokenProvider;
import com.blog.blograss.commons.response.Message;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/admin")
@RestController
public class AdminUserController {

    @Value("${admin.signup.secretKey}")
    private String ADMIN_SIGNUP_SECRETKEY;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @GetMapping("/userInfo")
    public ResponseEntity<Message> getUserInfo(HttpServletRequest req) {

        String accessToken = tokenProvider.extractAccessToken(req);

        Claims claims = tokenProvider.getClaims(accessToken);

        String adminId = claims.getSubject();

        return adminUserService.getUserInfo(adminId);
    }
    
    @PostMapping("/signup")
    public ResponseEntity<Message> postMethodName(@RequestBody AdminUserDto adminUserDto) {

        if(!ADMIN_SIGNUP_SECRETKEY.matches(adminUserDto.getSecretKey())) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(Message.write("UNAUTHORIZED_SECRET_KEY"));
        }

        if(adminUserService.getAdminUserById(adminUserDto.getAdminId()) != null) {
            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(Message.write("CONFLICT_ADMIN_ID"));
        }

        adminUserDto.setPassword(passwordEncoder.encode(adminUserDto.getPassword()));
        
        return adminUserService.insertAdminUser(adminUserDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Message> login(@RequestBody AdminUserDto adminUserDto) {

        return adminUserService.login(adminUserDto);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Message> logout(HttpServletRequest req) {

        String accessToken = tokenProvider.extractAccessToken(req);
        String refreshToken = tokenProvider.extractRefreshToken(req);

        if (!tokenProvider.validateToken(accessToken, req)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.write("ACCESSTOKEN_INVALID_ERR"));
        }
        if (!tokenProvider.validateToken(refreshToken, req)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.write("REFRESHTOKEN_INVALID_ERR"));
        }

        return adminUserService.logout(accessToken, refreshToken);
    }

    @GetMapping("/reissue")
    public ResponseEntity<Message> reissue(HttpServletRequest req) {

        String refreshToken = tokenProvider.extractRefreshToken(req);
        String accessToken = tokenProvider.extractAccessToken(req);

        if (tokenProvider.validateToken(accessToken, req)) {
            return ResponseEntity.badRequest().body(Message.write("ACCESSTOKEN_NOTEXPIRED_ERR"));
        }

        if (!tokenProvider.validateToken(refreshToken, req)) {
            return ResponseEntity.badRequest().body(Message.write("REFRESHTOKEN_INVALID_ERR"));
        }

        return adminUserService.reissue(accessToken, refreshToken);
    }
    
}
