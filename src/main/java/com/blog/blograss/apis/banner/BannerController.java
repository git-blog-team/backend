package com.blog.blograss.apis.banner;

import java.util.List;

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
import com.blog.blograss.apis.banner.object.BannerIdsDto;
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
public ResponseEntity<Message> deleteBanner(@RequestBody BannerIdsDto bannerIdsDto) {

    List<String> bannerIds = bannerIdsDto.getBannerIds();
    
    for (String bannerId : bannerIds) {
        
        BannerDto bannerDto = bannerMapper.getBanner(bannerId);

        if (bannerDto == null) {

        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(Message.write("NOT_FOUND"));

        }
    }
    
    return bannerService.deleteBanner(bannerIdsDto);
   
}

@GetMapping("/list")
public ResponseEntity<Message> getBannerList(@RequestParam("page") int page, @RequestParam("search") String search, @RequestParam("bannerType") String bannerType, @RequestParam("sortField") String sortField, @RequestParam("sortOrder") String sortOrder) { 

    if (!(sortField.equals("createdat") || sortField.equals("startedat") || sortField.equals("endedat") || sortField.equals(""))) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(Message.write("BAD_REQUEST"));
    }
    
    if(!(sortOrder.equals("DESC") || sortOrder.equals("ASC") || sortOrder.equals(""))) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(Message.write("BAD_REQUEST"));
    }

    return bannerService.getBannerList(page, bannerType, search, sortField, sortOrder);

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
