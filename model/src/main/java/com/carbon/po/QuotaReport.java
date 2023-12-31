package com.carbon.po;

import java.util.Date;

public class QuotaReport {
    Date date;
    String clientId;
    String productCode;//产品代码
    String productName;//产品名称
    String subjectMatterCode;//标的物代码
    String subjectMatterName;//标的物名称
    long openingAmount;//期初数量
    long endingAmount;//期末数量
    long availableAmount;//可用数量
    long transferableAmount;//可转出数量
    long buyAmount;//买入数量
    long sellAmount;//卖出数量
    long inAmount;//转入数量
    long outAmount;//转出数量
}
