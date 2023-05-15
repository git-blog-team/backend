package com.blog.blograss.apis.notice.object;

import lombok.Data;

@Data
public class NoticeDto {

    private String noticeId;
    private String title;
    private String content;
    private String createdAt;
    
}
