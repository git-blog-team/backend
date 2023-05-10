package com.blog.blograss.apis.notice;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.blog.blograss.apis.notice.object.NoticeDto;
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
    public ResponseEntity<Message> deleteNotice(String noticeId) {

        try {

            noticeMapper.deleteNotice(noticeId);

            return ResponseEntity.ok().body(Message.write("SUCCESS", noticeId));

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
