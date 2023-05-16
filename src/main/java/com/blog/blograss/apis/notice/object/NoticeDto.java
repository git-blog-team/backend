package com.blog.blograss.apis.notice.object;

import java.util.List;

import lombok.Data;

@Data
public class NoticeDto {

    private String noticeId;
    private String title;
    private String content;
    private String createdAt;
    private List<String> imageIds;
    
}
