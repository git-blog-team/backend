package com.blog.blograss.apis.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blograss.apis.notice.object.NoticeDto;
import com.blog.blograss.apis.notice.object.NoticeIdsDto;
import com.blog.blograss.commons.response.Message;


@RestController
@RequestMapping("/notice")
public class NoticeController {
    
    @Autowired
    NoticeMapper noticeMapper;

    @Autowired
    NoticeService noticeService;

@PostMapping
public ResponseEntity<Message> createNotice(@RequestBody NoticeDto noticeDto) {

    return noticeService.createNotice(noticeDto);

}

@GetMapping("/list")
public ResponseEntity<Message> getNoticeList(@RequestParam("search") String search, @RequestParam("page") int page, @RequestParam("sortField") String sortField, @RequestParam("sortOrder") String sortOrder) {


    if (!(sortField.equals("title") || sortField.equals("createdat") || sortField.equals(""))) {

        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(Message.write("Bad Request"));

    }

    if (!(sortOrder.equals("DESC") || sortOrder.equals("ASC") || sortOrder.equals(""))) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(Message.write("Bad Request"));
    }
    
    return noticeService.getNoticeList(search, page, sortField, sortOrder);

}

@GetMapping
public ResponseEntity<Message> getNotice(@RequestParam String noticeId) {

    return noticeService.getNotice(noticeId);

}

@DeleteMapping
public ResponseEntity<Message> deleteNotice(@RequestBody NoticeIdsDto noticeIdsDto) {

    List<String> noticeIds = noticeIdsDto.getNoticeIds();

    for(String noticeId : noticeIds) {

        NoticeDto getNotice = noticeMapper.getNotice(noticeId);

        if (getNotice == null) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(Message.write("Not Found"));
        }
    }

    return noticeService.deleteNotice(noticeIdsDto);

}

@PutMapping
public ResponseEntity<Message> updateNotice(@RequestBody NoticeDto noticeDto) {

    String noticeId = noticeDto.getNoticeId();

    NoticeDto getNotice = noticeMapper.getNotice(noticeId);

    if (getNotice == null) {
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(Message.write("Not Found"));
    }

    return noticeService.updateNotice(noticeDto, noticeId);
}

}
