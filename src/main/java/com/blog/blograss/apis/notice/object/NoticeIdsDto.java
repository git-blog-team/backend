package com.blog.blograss.apis.notice.object;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoticeIdsDto {
    
    private List<String> noticeIds;

}
