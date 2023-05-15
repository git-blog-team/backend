package com.blog.blograss.apis.notice;

import org.springframework.http.ResponseEntity;

import com.blog.blograss.apis.notice.object.NoticeDto;
import com.blog.blograss.apis.notice.object.NoticeIdsDto;
import com.blog.blograss.commons.response.Message;

public interface NoticeService {
    
    ResponseEntity<Message> createNotice(NoticeDto notice);

    ResponseEntity<Message> updateNotice(NoticeDto notice, String noticeId);
    
    ResponseEntity<Message> getNotice(String noticeId);

    ResponseEntity<Message> deleteNotice(NoticeIdsDto noticeIdDto);

    ResponseEntity<Message> getNoticeList(String search, int page, String sortField, String sortOrder);

}
