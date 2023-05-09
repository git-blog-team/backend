package com.blog.blograss.apis.banner;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.blog.blograss.apis.banner.object.BannerDto;

@Mapper
public interface BannerMapper {

    void createBanner(BannerDto banner);

    void deleteBanner(String bannerId);

    void updateBanner(BannerDto banner);

    BannerDto getBanner(String bannerId);
    
    List<BannerDto> getBannerList(int offset);
    
    int getTotalCount();

}

