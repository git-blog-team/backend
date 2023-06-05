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
import com.blog.blograss.apis.report.object.ReportListParamDto;
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
    
    @GetMapping("/post/list")
    public ResponseEntity<Message> getReportListPost(
        @RequestParam String search,
        @RequestParam Integer page,
        @RequestParam String type,
        @RequestParam String status,
        @RequestParam String sortField,
        @RequestParam String sortOrder,
        @RequestParam Integer rowCount
    ) {

        if(sortField == null || sortField.matches("") && sortOrder == null || sortOrder.matches("")) {
            sortField = "createdat";
            sortOrder = "DESC";
        }

        if(rowCount == null) {
            rowCount = 20;
        }

        Integer offset = (page - 1) * rowCount;

        ReportListParamDto reportListParamDto = ReportListParamDto.builder()
                .search(search)
                .offset(offset)
                .type(type)
                .status(status)
                .sortField(sortField)
                .sortOrder(sortOrder)
                .limit(rowCount)
                .build();

        return reportService.getReportListPost(reportListParamDto);
    }

    @GetMapping("/comment/list")
    public ResponseEntity<Message> getReportListComment(
        @RequestParam String search,
        @RequestParam Integer page,
        @RequestParam String type,
        @RequestParam String status,
        @RequestParam String sortField,
        @RequestParam String sortOrder,
        @RequestParam Integer rowCount
    ) {

        if(sortField == null || sortField.matches("") && sortOrder == null || sortOrder.matches("")) {
            sortField = "createdat";
            sortOrder = "DESC";
        }

        if(rowCount == null) {
            rowCount = 20;
        }

        Integer offset = (page - 1) * rowCount;

        ReportListParamDto reportListParamDto = ReportListParamDto.builder()
                .search(search)
                .offset(offset)
                .type(type)
                .status(status)
                .sortField(sortField)
                .sortOrder(sortOrder)
                .limit(rowCount)
                .build();

        return reportService.getReportListComment(reportListParamDto);
    }

    @GetMapping
    public ResponseEntity<Message> getReportDetailPost(
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
