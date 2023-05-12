package com.blog.blograss.apis.banner.object;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BannerListQueryDto {

    private int offset;
    private String search;
    private String bannerType;
    private String sortField;
    private String sortOrder;

}
