package com.carbon.po;
import java.util.Date;
import java.util.List;
public class Report {
    Date date;
    Date printDate;
    String clientId;
    String clientName;
    String groupCode;//集团代码
    String groupName;//集团名称
    String capitalAccountId;
    String quotaAccountId;
    double openingCapital;//期初余额
    double endingCapital;//期末余额
    double availableCapital;//可用资金
    double transferCapital;//可出资金
    double buyCapital;//买入资金
    double sellCapital;//卖出资金
    double buyCommission;//买入手续费
    double sellCommission;//卖出手续费
    double inCapital;//转入资金
    double outCapital;//转出资金
    double auctionRegistration;//拍卖报名费
    double settlementCommission;//结算手续费
    List<QuotaReport> quotaReportList;
}
