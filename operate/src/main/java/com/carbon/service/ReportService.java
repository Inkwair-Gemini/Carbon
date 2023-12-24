package com.carbon.service;

import com.carbon.po.Report;

import java.util.Date;

public interface ReportService {
    // 生成报表
    public Report createReport();
    // 打印报表
    public void printReport();
    // 获取报表
    public Report getReport(Date date,String clientId);
}
