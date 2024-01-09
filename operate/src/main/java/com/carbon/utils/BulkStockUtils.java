package com.carbon.utils;

import com.carbon.po.BulkAgreement.DirectionDoneRecord;
import com.carbon.po.BulkAgreement.GroupDoneRecord;

import java.util.List;

/**
 * 大宗交易工具类
 */
public class BulkStockUtils {
    public static Double getDirectionClosingPrice(List<DirectionDoneRecord> results) {
        // 检查结果列表是否为空
        if (results == null || results.isEmpty()) {
            return null;
        }

        // 获取最后一个结果，即收盘时的结果
        DirectionDoneRecord lastResult = results.get(results.size() - 1);

        // 返回收盘价
        return lastResult.getFinallyPrice();
    }

    public static Double getGroupClosingPrice(List<GroupDoneRecord> results) {
        // 检查结果列表是否为空
        if (results == null || results.isEmpty()) {
            return null;
        }

        // 获取最后一个结果，即收盘时的结果
        GroupDoneRecord lastResult = results.get(results.size() - 1);

        // 返回收盘价
        return lastResult.getFinallyPrice();
    }

    public static Double[] getClosingPriceRange(Double closingPrice) {
        // 检查收盘价是否为空
        if (closingPrice == null) {
            return null;
        }

        // 计算收盘价的±30%
        double lowerBound = closingPrice * 0.7;
        double upperBound = closingPrice * 1.3;

        // 返回结果
        return new Double[]{lowerBound, upperBound};
    }
}
