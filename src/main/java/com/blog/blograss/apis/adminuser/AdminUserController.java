package com.blog.blograss.apis.adminuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blograss.apis.adminuser.object.AdminUserDto;
import com.blog.blograss.commons.response.Message;


@RequestMapping("/admin")
@RestController
public class AdminUserController {

    @Value("${admin.signup.secretKey}")
    private String ADMIN_SIGNUP_SECRETKEY;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminUserService adminUserService;
    
    @PostMapping(value="/signup")
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
    
}
