package com.blog.blograss.apis.banner;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blograss.apis.banner.object.BannerDto;



@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    BannerMapper bannerMapper;

@PostMapping("")
public ResponseEntity<BannerDto> createBanner(@RequestBody BannerDto bannerDto) {

    try {
        String bannerId = UUID.randomUUID().toString();
        
        bannerDto.setBannerId(bannerId);

        bannerMapper.createBanner(bannerDto);

        BannerDto createdBannerDto = bannerMapper.getBanner(bannerId);

        return new ResponseEntity<>(createdBannerDto, HttpStatus.CREATED);

    } catch(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}

@GetMapping("/")
public ResponseEntity<BannerDto> getBanner(@RequestParam String bannerId) {
    try {
        BannerDto getBannerDto = bannerMapper.getBanner(bannerId);
        if (getBannerDto != null) {
            return new ResponseEntity<>(getBannerDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    } catch(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


}
