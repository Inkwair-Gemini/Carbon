package com.carbon.service;

public interface TradeSelectService {
    //查询当日交易
    List selectTodayTrade();
    //查询历史交易
    List selectHistoryTrade();
}