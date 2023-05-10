package com.blog.blograss.apis.notice;

import org.apache.ibatis.annotations.Mapper;

import com.blog.blograss.apis.notice.object.NoticeDto;

@Mapper
public interface NoticeMapper {
    
    void updateNotice(NoticeDto notice);

    void createNotice(NoticeDto notice);

    NoticeDto getNotice(String noticeId);

    NoticeDto deleteNotice(String noticeId);
    
}
