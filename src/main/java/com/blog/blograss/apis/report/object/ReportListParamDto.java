package com.blog.blograss.apis.report.object;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportListParamDto {

    private Integer offset;
    private String target;
    private String type;
    private String status;
    private String sortField;
    private String sortOrder;
    private String search;
}
