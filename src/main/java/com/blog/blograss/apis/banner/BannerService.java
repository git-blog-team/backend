package com.blog.blograss.apis.banner;

import org.springframework.http.ResponseEntity;

import com.blog.blograss.apis.banner.object.BannerDto;
import com.blog.blograss.apis.banner.object.BannerIdsDto;
import com.blog.blograss.commons.response.Message;

public interface BannerService {
    
    ResponseEntity<Message> createBanner(BannerDto banner);
    
    ResponseEntity<Message> getBanner(String bannerId);

    ResponseEntity<Message> deleteBanner(BannerIdsDto bannerIds);

    ResponseEntity<Message> getBannerList(int offset, String bannerType, String search, String sortOrder, String sortField);

    ResponseEntity<Message> updateBanner(BannerDto banner, String BannerId);
}
