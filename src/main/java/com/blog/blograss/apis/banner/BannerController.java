package com.blog.blograss.apis.banner;

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

    @Autowired
    BannerService bannerService;

@PostMapping
public ResponseEntity<Message> createBanner(@RequestBody BannerDto bannerDto) {
    
    return bannerService.createBanner(bannerDto);
    
}

@GetMapping
public ResponseEntity<Message> getBanner(@RequestParam String bannerId) {

    return bannerService.getBanner(bannerId);

}

@DeleteMapping
public ResponseEntity<Message> deleteBanner(@RequestBody BannerDto bannerDto) {
    String bannerId = bannerDto.getBannerId();
    BannerDto getBannerDto = bannerMapper.getBanner(bannerId);
    if(getBannerDto == null) {
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(Message.write("NOT_FOUND"));
    }
   return bannerService.deleteBanner(bannerDto, bannerId);
}

@GetMapping("/list")
public ResponseEntity<Message> getBannerList(@RequestParam("page") int page) {

    return bannerService.getBannerList(page);

} 

@PutMapping
public ResponseEntity<Message> updateBanner(@RequestBody BannerDto bannerDto) {

    String bannerId = bannerDto.getBannerId();
    BannerDto getBannerDto = bannerMapper.getBanner(bannerId);
    
    if(getBannerDto == null) {
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(Message.write("NOT_FOUND"));
    }
        
    return bannerService.updateBanner(bannerDto, bannerId);
}
}
