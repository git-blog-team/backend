package com.blog.blograss.apis.banner;

import org.apache.ibatis.annotations.Mapper;

import com.blog.blograss.apis.banner.object.BannerDto;

@Mapper
public interface BannerMapper {
    void createBanner(BannerDto banner);
    BannerDto getBanner(String bannerId);
}
