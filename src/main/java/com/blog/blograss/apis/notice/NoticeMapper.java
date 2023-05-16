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

    void insertNoticeImage(@Param("noticeId") String noticeId, @Param("imageId") String imageId);

    int getNoticeCount(@Param("search") String search);

    NoticeDto getNotice(String noticeId);

    void deleteNotice(NoticeIdsDto noticeIds);
    
    void deleteNoticeImage(NoticeIdsDto noticeIds);

    List<NoticeDto> getNoticeList(NoticeListQueryDto noticeListDto);

    List<String> getNoticeImageIdsToDelete(NoticeIdsDto noticeIds);
}
