package com.carbon.service.Impl;

import com.carbon.po.Report;
import com.carbon.service.ReportService;
import java.util.Date;

public class ReportServiceImpl implements ReportService {

    @Override
    public Report createReport() {
        // TODO 创建报表
    }

    @Override
    public void printReport() {
        // TODO 打印报表
    }

    @Override
    public Report getReport(Date date,String clientId) {
        Report report = ReportDao.getReport(date,String clientId);
        return report;
    }
}
