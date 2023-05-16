package com.blog.blograss.apis.banner.object;

import lombok.Data;

@Data
public class BannerDto {

    private String bannerId;
    private String bannerName;
    private String bannerType;
    private String imageId;
    private String createdAt;
    private String startedAt;
    private String endedAt;

    // public BannerDto() {}

    // public BannerDto(String bannerId, String bannerName, String bannerType, String url, String createdAt, String startedAt, String endedAt) {
    //     this.bannerId = bannerId;
    //     this.bannerName = bannerName;
    //     this.bannerType = bannerType;
    //     this.url = url;
    //     this.createdAt = createdAt;
    //     this.endedAt = endedAt;
    //     this.startedAt = startedAt;
    // }

    // public String getBannerId() {
    //     return bannerId;
    // }

    // public void setBannerId(String bannerId) {
    //     this.bannerId = bannerId;
    // }

    // public String getBannerName() {
    //     return bannerName;
    // }

    // public void setBannerName(String bannerName) {
    //     this.bannerName = bannerName;
    // }

    // public String getBannerType() {
    //     return bannerType;
    // }

    // public void setBannerType(String bannerType) {
    //     this.bannerType = bannerType;
    // }

    // public String getUrl() {
    //     return url;
    // }

    // public void setUrl(String url) {
    //     this.url = url;
    // }

    // public  String getCreatedAt() {
    //     return createdAt;
    // }

    // public void setCreatedAt(String createdAt) {
    //     this.createdAt = createdAt;
    // }

    // public String getStartedAt() {
    //     return startedAt;
    // }

    // public void setStartedAt(String startedAt) {
    //     this.startedAt = startedAt;
    // }

    // public String getEndedAt() {
    //     return endedAt;
    // }

    // public void setEndedAt(String endedAt) {
    //     this.endedAt = endedAt;
    // }
}
