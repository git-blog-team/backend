package com.blog.blograss.apis.report;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.blograss.apis.adminuser.AdminUserMapper;
import com.blog.blograss.apis.report.object.ReportDto;
import com.blog.blograss.apis.report.object.ReportListParamDto;
import com.blog.blograss.apis.report.object.Status;
import com.blog.blograss.apis.report.object.Target;
import com.blog.blograss.commons.response.Message;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public ResponseEntity<Message> getReportList(ReportListParamDto reportListParamDto) {
        
        try {
            return ResponseEntity.ok().body(Message.write("SUCCESS", reportMapper.getReportList(reportListParamDto), reportMapper.getReportListCount(reportListParamDto)));
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e.toString()));
        }
    }

    @Override
	public ResponseEntity<Message> getReportDetail(String reportId) {

        String target = reportMapper.getReportTargetById(reportId);

        if(target == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.write("NOT_FOUND_TARGET"));
        }

        try {
            if(target.matches("COMMENT")) {
                return ResponseEntity.ok().body(Message.write("SUCCESS",  reportMapper.getReportTargetDetailByComment(reportId)));
            } else {
                return ResponseEntity.ok().body(Message.write("SUCCESS",  reportMapper.getReportTargetDetailByPost(reportId)));
            }
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
	}

    @Override
    @Transactional
    public ResponseEntity<Message> acceptReport(ReportDto reportDto, String adminId) {
        
        // 유효한 계정인지 확인
        if(adminUserMapper.getAdminUserById(adminId) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.write("NOT_ADMIN_ERR"));
        }
        ReportDto findReportDto = reportMapper.getReportById(reportDto.getReportId());
        // 신고글이 있는지 확인
        if(findReportDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.write("NOT_FOUND_REPORT"));
        }

        if(findReportDto.getStatus().equals(Status.DENY)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Message.write("ALREADY_DENY_ERR"));
        }

        try {
            Map<String, Object> findTargetMap = null;

            if(findReportDto.getTarget().equals(Target.COMMENT)) {
                findTargetMap = reportMapper.getCommentById(findReportDto.getTargetId());
            } else if(findReportDto.getTarget().equals(Target.POST)) {
                findTargetMap = reportMapper.getPostById(findReportDto.getTargetId());
            }
    
            // 승인 대기
            if(findReportDto.getStatus().equals(Status.PENDING)) {
                // 신고상태 승인으로 변경
                LocalDateTime curDate = LocalDateTime.now();
                reportDto.setStatus(Status.APPROVAL);
                reportDto.setSolvedAt(curDate);
                reportMapper.updateReportStatus(reportDto);
    
                Map<String, Object> targetMap = new HashMap<String, Object>();
                targetMap.put("targetId", findReportDto.getTargetId());
                targetMap.put("reportCount", 1);
                targetMap.put("reportType", "plus");
    
                // 게시물 또는 댓글 상태 신고로 변경
                if(findReportDto.getTarget().equals(Target.COMMENT)) {
                    reportMapper.updateCommentReport(targetMap);
                } else if(findReportDto.getTarget().equals(Target.POST)) {
                    reportMapper.updatePostReport(targetMap);
                }
    
                // 유저 신고 횟수 카운트 업
                Map<String, Object> userMap = new HashMap<String, Object>();
                userMap.put("userId", findTargetMap.get("userid"));
                userMap.put("reportCount", 1);
                userMap.put("reportType", "plus");
                reportMapper.updateUserReportCount(userMap);
            }
    
            // 승인 됨
            if(findReportDto.getStatus().equals(Status.APPROVAL)) {
                // 신고상태 승인대기로 변경
                reportDto.setStatus(Status.PENDING);
                reportDto.setSolvedAt(null);
                reportMapper.updateReportStatus(reportDto);
    
                // 게시물 또는 댓글 상태 노신고로 변경
                Map<String, Object> targetMap = new HashMap<String, Object>();
                targetMap.put("targetId", findReportDto.getTargetId());
                targetMap.put("reportCount", 1);
                targetMap.put("reportType", "minus");
    
                // 게시물 또는 댓글 상태 신고로 변경
                if(findReportDto.getTarget().equals(Target.COMMENT)) {
                    reportMapper.updateCommentReport(targetMap);
                } else if(findReportDto.getTarget().equals(Target.POST)) {
                    reportMapper.updatePostReport(targetMap);
                }
    
                // 유저 신고 횟수 카운트 마이너스
                Map<String, Object> userMap = new HashMap<String, Object>();
                userMap.put("userId", findTargetMap.get("userid"));
                userMap.put("reportCount", 1);
                userMap.put("reportType", "minus");

                reportMapper.updateUserReportCount(userMap);
            }
    
            return ResponseEntity.ok().body(Message.write("SUCCESS"));
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("ERROR", e));
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Message> denyReport(ReportDto reportDto, String adminId) {
        
        // 유효한 계정인지 확인
        if(adminUserMapper.getAdminUserById(adminId) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message.write("NOT_ADMIN_ERR"));
        }
        ReportDto findReportDto = reportMapper.getReportById(reportDto.getReportId());
        // 신고글이 있는지 확인
        if(findReportDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.write("NOT_FOUND_REPORT"));
        }

        if(findReportDto.getStatus().equals(Status.APPROVAL)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Message.write("ALREADY_APPROVAL_ERR"));
        }

        try {
            // 승인대기
            if(findReportDto.getStatus().equals(Status.PENDING)) {
                // 신고상태 승인거부로 변경
                LocalDateTime curDate = LocalDateTime.now();
                reportDto.setStatus(Status.DENY);
                reportDto.setSolvedAt(curDate);
                reportMapper.updateReportStatus(reportDto);
            }
    
            // 승인거부
            if(findReportDto.getStatus().equals(Status.DENY)) {
                // 신고상태 승인대기로 변경
                reportDto.setStatus(Status.PENDING);
                reportDto.setSolvedAt(null);
                reportMapper.updateReportStatus(reportDto);
            }

            return ResponseEntity.ok().body(Message.write("SUCCESS"));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("ERROR", e));
        }
    }

    @Override
    public ResponseEntity<Message> insertReport(ReportDto reportDto) {
        
        if(
            reportDto.getStatus() == null || 
            !reportDto.getStatus().equals(Status.APPROVAL) && 
            !reportDto.getStatus().equals(Status.PENDING) && 
            !reportDto.getStatus().equals(Status.DENY) || 
            reportDto.getTarget() == null || 
            !reportDto.getTarget().equals(Target.COMMENT) &&
            !reportDto.getTarget().equals(Target.POST) ||
            reportDto.getType() == null ||
            reportDto.getType().matches("")
        ) {
            return ResponseEntity.badRequest().body(Message.write("REQUIRE_PARAMETER_ERR"));
        }

        if(reportDto.getTarget().equals(Target.COMMENT)) {
            Map<String, Object> map = reportMapper.getCommentById(reportDto.getTargetId());
            if(map == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.write("NOT_FOUND_COMMNET"));
            }
        } else if(reportDto.getTarget().equals(Target.POST)) {
            Map<String, Object> map = reportMapper.getPostById(reportDto.getTargetId());
            if(map == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.write("NOT_FOUND_POST"));
            }
        }

        String uuid = UUID.randomUUID().toString();

        reportDto.setReportId(uuid);

        try {
            reportMapper.insertReport(reportDto);
            return ResponseEntity.ok().body(Message.write("SUCCESS"));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write(e.toString()));
        }
    }
    
}
