package com.blog.blograss.apis.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blograss.apis.report.object.ReportDto;
import com.blog.blograss.commons.jwt.JwtTokenProvider;
import com.blog.blograss.commons.response.Message;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ReportService reportService;
    
    @GetMapping("/list")
    public ResponseEntity<Message> getReportList(
        @RequestParam Integer page,
        @RequestParam String target,
        @RequestParam String type,
        @RequestParam String status,
        @RequestParam String order
    ) {
        return null;
    }

    @GetMapping
    public ResponseEntity<Message> getReportDetail(
        @RequestParam String reportId
    ) {
        return reportService.getReportDetail(reportId);
    }

    @PostMapping("/accept")
    public ResponseEntity<Message> acceptReport(
        @RequestBody ReportDto reportDto,
        HttpServletRequest req
    ) {
        String accessToken = jwtTokenProvider.extractAccessToken(req);

        Claims claims = jwtTokenProvider.getClaims(accessToken);

        String adminId = claims.getSubject();

        return reportService.acceptReport(reportDto, adminId);
    }

    @PostMapping("/deny")
    public ResponseEntity<Message> denyReport(
        @RequestBody ReportDto reportDto,
        HttpServletRequest req
    ) {
        String accessToken = jwtTokenProvider.extractAccessToken(req);

        Claims claims = jwtTokenProvider.getClaims(accessToken);

        String adminId = claims.getSubject();

        return reportService.denyReport(reportDto, adminId);
    }

    @PostMapping
    public ResponseEntity<Message> insertReport(
        @RequestBody ReportDto reportDto,
        HttpServletRequest req
    ) {
        String accessToken = jwtTokenProvider.extractAccessToken(req);

        Claims claims = jwtTokenProvider.getClaims(accessToken);

        String userId = claims.getSubject();

        reportDto.setUserId(userId);

        return reportService.insertReport(reportDto);
    }
}
