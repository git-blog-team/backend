package com.blog.blograss.apis.report;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.blog.blograss.apis.report.object.ReportDto;
import com.blog.blograss.apis.report.object.ReportListParamDto;

@Mapper
public interface ReportMapper {

    List<ReportDto> getReportList(ReportListParamDto reportListParamDto);

    Integer getReportListCount(ReportListParamDto reportListParamDto);

    ReportDto getReportById(String reportId);

    Map<String, Object> getPostById(String targetId);

    Map<String, Object> getCommentById(String targetId);

    Map<String, Object> getReportTargetDetailByPost(String reportId);

    Map<String, Object> getReportTargetDetailByComment(String reportId);

    String getReportTargetById(String reportId);

    void insertReport(ReportDto reportDto);

    void updateReportStatus(ReportDto reportDto);

    void updatePostReport(Map<String, Object> map);

    void updateCommentReport(Map<String, Object> map);

    void updateUserReportCount(Map<String, Object> map);
}
