package com.blog.blograss.apis.notice.object;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoticeListQueryDto {
    
    private int offset;
    private int rowCount;
    private String search;
    private String sortField;
    private String sortOrder;

}
