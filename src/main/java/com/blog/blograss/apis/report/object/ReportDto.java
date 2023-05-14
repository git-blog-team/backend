package com.blog.blograss.apis.report.object;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReportDto {
    
    private String reportId;
    private String type;
    private Status status;
    private Target target;
    private String targetId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime solvedAt;
    private String userId;

}
