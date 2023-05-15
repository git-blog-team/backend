package com.blog.blograss.apis.notice;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.blog.blograss.apis.notice.object.NoticeDto;
import com.blog.blograss.apis.notice.object.NoticeIdsDto;
import com.blog.blograss.apis.notice.object.NoticeListQueryDto;
import com.blog.blograss.commons.response.Message;

@Service
public class NoticeServiceImpl implements NoticeService {
    
    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public ResponseEntity<Message> createNotice(NoticeDto noticeDto) {

        try {

            String noticeId = UUID.randomUUID().toString();

            noticeDto.setNoticeId(noticeId);

            noticeMapper.createNotice(noticeDto);

            NoticeDto getNotice = noticeMapper.getNotice(noticeId);

            if (getNotice == null) {
                return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR"));
            }

            return ResponseEntity.ok().body(Message.write("SUCCESS", getNotice));

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR", e));

        }

    }

    @Override
    public ResponseEntity<Message> getNoticeList(String search, int page, String sortField, String sortOrder) {

        int offset = (page - 1) * 20;

        NoticeListQueryDto noticeListQueryDto = NoticeListQueryDto.builder()
                .search(search)
                .offset(offset)
                .sortField(sortField)
                .sortOrder(sortOrder)
                .build();
        try {

            List<NoticeDto> getNoticeList = noticeMapper.getNoticeList(noticeListQueryDto);

            int count = noticeMapper.getNoticeCount(search);

            return ResponseEntity.ok().body(Message.write("SUCCESS", getNoticeList, count));

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR", e));

        }
    }
    
    @Override
    public ResponseEntity<Message> getNotice(String noticeId) {
        try {

            NoticeDto getNotice = noticeMapper.getNotice(noticeId);

            if (getNotice == null) {
                return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(Message.write("NOT_FOUND"));
            }

            return ResponseEntity.ok().body(Message.write("SUCCESS", getNotice));

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR", e));

        }
    }
    
    @Override
    public ResponseEntity<Message> deleteNotice(NoticeIdsDto noticeIdsDto) {

        try {

            noticeMapper.deleteNotice(noticeIdsDto);

            return ResponseEntity.ok().body(Message.write("SUCCESS", noticeIdsDto));

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR", e));

        }

    }
    
    @Override
    public ResponseEntity<Message> updateNotice(NoticeDto noticeDto, String noticeId) {

        try {

            noticeMapper.updateNotice(noticeDto);

            NoticeDto getNotice = noticeMapper.getNotice(noticeId);

            if (getNotice == null) {
                
                return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR"));

            }

            return ResponseEntity.ok().body(Message.write("SUCCESS", getNotice));

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR", e));

        }
    }
    
}
