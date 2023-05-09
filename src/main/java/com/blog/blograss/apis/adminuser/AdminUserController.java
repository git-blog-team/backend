package com.blog.blograss.apis.adminuser;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blograss.apis.adminuser.object.AdminUserDto;
import com.blog.blograss.commons.response.Message;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequestMapping("/admin")
@RestController
public class AdminUserController {
    
    @PostMapping(value="/signup")
    public ResponseEntity<Message> postMethodName(@RequestBody AdminUserDto adminUserDto) {
        // example
        // ResponseEntity.badRequest().body(Message.write("400: BAD_REQUEST"));
        // ResponseEntity.status(HttpStatusCode.valueOf(404)).body(Message.write("NOT_FOUND"));
        // ResponseEntity.ok().body(Message.write("SUCESS", list))
        // ResponseEntity.status(HttpStatusCode.valueOf(200)).body(Message.write("SUCESS", adminUserDto));
        return ResponseEntity.ok().body(Message.write("SUCESS"));
    }
    
}
