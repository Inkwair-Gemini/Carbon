package com.carbon.StockUtils;

import com.carbon.po.DirectionDoneRecord;

import java.util.List;

/**
 * @projectName: Carbon
 * @package: com.carbon.StockUtils
 * @className: BulkStockUtils
 * @author: Doctor.H
 * @description:  大宗交易工具类
 * @date: 2024/1/5 16:09
 */
public class BulkStockUtils {
    public static Double getClosingPrice(List<DirectionDoneRecord> results) {
        // 检查结果列表是否为空
        if (results == null || results.isEmpty()) {
            return null;
        }

        // 获取最后一个结果，即收盘时的结果
        DirectionDoneRecord lastResult = results.get(results.size() - 1);

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
