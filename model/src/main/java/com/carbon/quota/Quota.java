package com.carbon.quota;

// 配额
enum Quota {
    peie1("INK", "测试标的物1"),
    peie2("JUN", "测试标的物2"),
    peie3("HONG","测试标的物3"),
    peie4("ZHOU", "测试标的物4"),
    peie5("YUAN", "测试标的物5");

    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称

    Quota(String subjectMatterCode, String subjectMatterName) {
        this.subjectMatterCode = subjectMatterCode;
        this.subjectMatterName = subjectMatterName;
    }
}