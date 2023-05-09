package com.blog.blograss.apis.banner;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blograss.apis.banner.object.BannerDto;
import com.blog.blograss.commons.response.Message;



@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    BannerMapper bannerMapper;

@PostMapping
public ResponseEntity<Message> createBanner(@RequestBody BannerDto bannerDto) {
    
    try {
        String bannerId = UUID.randomUUID().toString();
        
        bannerDto.setBannerId(bannerId);

        bannerMapper.createBanner(bannerDto);

        BannerDto createdBannerDto = bannerMapper.getBanner(bannerId);

        return ResponseEntity.ok().body(Message.write("SUCCESS", createdBannerDto));

    } catch(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR",e));
    }
    
}

@GetMapping
public ResponseEntity<Message> getBanner(@RequestParam String bannerId) {

    try {

        BannerDto getBannerDto = bannerMapper.getBanner(bannerId);
        if (getBannerDto != null) {
            return ResponseEntity.ok().body(Message.write("SUCCESS", getBannerDto));
        } else {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(Message.write("NOT_FOUND"));
        }

    } catch(Exception e) {

        e.printStackTrace();
        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR",e));

    }
}

@DeleteMapping
public ResponseEntity<Message> deleteBanner(@RequestBody BannerDto bannerDto) {

    try {
        String bannerId = bannerDto.getBannerId();
        bannerMapper.deleteBanner(bannerId);
        return ResponseEntity.ok().body(Message.write("SUCCESS", bannerId));

    } catch(Exception e) {

        e.printStackTrace();
        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR",e));

    }
}

@GetMapping("/list")
public ResponseEntity<Message> getBannerList(@RequestParam("page") int page) {

    try {
        int offset = (page -1) * 20;
        List<BannerDto> getBannerList = bannerMapper.getBannerList(offset);
        int totalCount = bannerMapper.getTotalCount();
        if(getBannerList != null) {
            return ResponseEntity.ok().body(Message.write("SUCCESS", getBannerList, totalCount));
        } else {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(Message.write("NOT_FOUND"));
        }
        
    } catch(Exception e) {

        e.printStackTrace();
        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR",e));

    }
} 

@PutMapping
public ResponseEntity<Message> updateBanner(@RequestBody BannerDto bannerDto) {
    String bannerId = bannerDto.getBannerId();
    BannerDto getBannerDto = bannerMapper.getBanner(bannerId);
    if(getBannerDto == null) {
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(Message.write("NOT_FOUND"));
    }
        try {
            bannerMapper.updateBanner(bannerDto);
            BannerDto updatedBannerDto = bannerMapper.getBanner(bannerId);
            return ResponseEntity.ok().body(Message.write("SUCCESS", updatedBannerDto));
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR"));
        }
    
}
}
