package com.carbon.po;'资金交易流水
class CapitalTradeRecord{
- id:String
- date:String
- time:String
- operator:String '操作员代码
- capitalAccount:String '资金账号
- type:String '划转类型
- actualAmount:double '发生金额
- endingBalance:double '期后余额
- endingAvailableBalance:double '期后可用余额
}