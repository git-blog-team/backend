package com.blog.blograss.apis.notice;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.blog.blograss.apis.notice.object.NoticeDto;
import com.blog.blograss.apis.notice.object.NoticeIdsDto;
import com.blog.blograss.apis.notice.object.NoticeListQueryDto;

@Mapper
public interface NoticeMapper {
    
    void updateNotice(NoticeDto notice);

    void createNotice(NoticeDto notice);

    List<NoticeDto> getNoticeList(NoticeListQueryDto noticeListDto);

    int getNoticeCount(@Param("search") String search);

    NoticeDto getNotice(String noticeId);

    void deleteNotice(NoticeIdsDto noticeIds);
    
}
