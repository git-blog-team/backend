package com.blog.blograss.apis.banner;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.blog.blograss.apis.banner.object.BannerDto;
import com.blog.blograss.apis.banner.object.BannerIdsDto;
import com.blog.blograss.apis.banner.object.BannerListQueryDto;

@Mapper
public interface BannerMapper {

    void createBanner(BannerDto banner);

    void deleteBanner(BannerIdsDto bannerIds);

    void updateBanner(BannerDto banner);

    BannerDto getBanner(String bannerId);

    List<BannerDto> getBannerList(BannerListQueryDto bannerListQuery);

    int getTotalCount(@Param("bannerType") String bannerType, @Param("search") String search);

    List<String> getBannerImageIdsToDelete(BannerIdsDto bannerIds);

}



