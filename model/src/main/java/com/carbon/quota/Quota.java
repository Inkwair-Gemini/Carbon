package com.carbon.quota;

// 配额
enum Quota {
    peie1("303", "peie1"),
    peie2("304", "peie2"),
    peie3("305","peie3");

    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称

    Quota(String subjectMatterCode, String subjectMatterName) {
        this.subjectMatterCode=subjectMatterCode;
        this.subjectMatterName=subjectMatterName;
    }
}