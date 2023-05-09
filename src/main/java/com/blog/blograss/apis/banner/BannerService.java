package com.blog.blograss.apis.banner;

import org.springframework.http.ResponseEntity;

import com.blog.blograss.apis.banner.object.BannerDto;
import com.blog.blograss.commons.response.Message;

public interface BannerService {
    ResponseEntity<Message> createBanner(BannerDto banner);
    
    ResponseEntity<Message> getBanner(String bannerId);

    ResponseEntity<Message> deleteBanner(BannerDto banner, String BannerId);

    ResponseEntity<Message> getBannerList(int page);

    ResponseEntity<Message> updateBanner(BannerDto banner, String BannerId);
}
