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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ReportService reportService;
    
    @Operation(summary = "신고 목록 조회", description = "신고글 목록을 조회해옵니다.", tags = {"Report Controller"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
        @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @GetMapping("/list")
    public ResponseEntity<Message> getReportList(
        @Parameter(name = "search", description = "신고한 유저 검색", required = true, example = "uiop5487@gmail.com") @RequestParam String search,
        @RequestParam Integer page,
        @RequestParam String target,
        @RequestParam String type,
        @RequestParam String status,
        @RequestParam String sortField,
        @RequestParam String sortOrder
    ) {

        if(sortField == null || sortField.matches("") && sortOrder == null || sortOrder.matches("")) {
            sortField = "createdat";
            sortOrder = "DESC";
        }

        Integer offset = (page - 1) * 20;

        ReportListParamDto reportListParamDto = ReportListParamDto.builder()
                .search(search)
                .offset(offset)
                .target(target)
                .type(type)
                .status(status)
                .sortField(sortField)
                .sortOrder(sortOrder)
                .build();

        return reportService.getReportList(reportListParamDto);
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
