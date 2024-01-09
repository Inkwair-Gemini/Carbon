package com.carbon.controller;

import com.carbon.input.CapitalAndQuota.CapitalTransferPost;
import com.carbon.po.Capital.CapitalAccount;
import com.carbon.po.Capital.CapitalTradeRecord;
import com.carbon.po.Capital.DepositAndWithdrawalRecord;
import com.carbon.po.Capital.DepositAndWithdrawalRequestRecord;
import com.carbon.result.Result;
import com.carbon.service.CapitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/capital")
public class CapitalController {
    @Autowired
    CapitalService capitalService;

    //资金转入/转出  接受资金划转表单 实现资金账户与银行的资金划转 返回值ok()
    @PostMapping("/transfer")
    public Result QuotaTransfer(@RequestBody CapitalTransferPost capitalTransferPost) {
        try {
            String type = capitalTransferPost.getType();
            if (type.equals("转入"))
                capitalService.capitalIn(capitalTransferPost.getCapitalAccount(), capitalTransferPost.getActualAmount());
            else if (type.equals("转出"))
                capitalService.capitalOut(capitalTransferPost.getCapitalAccount(), capitalTransferPost.getActualAmount());
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }

    //查询资金账号 接受资金账号ID  查询资金账号详细信息  返回资金账号详细信息
    @GetMapping("/selectCapitalAccount/{accountId}")
    public Result SelectCapitalAccount(@PathVariable String accountId) {
        CapitalAccount capitalAccount = capitalService.selectCapitalAccount(accountId);
        return Result.ok(capitalAccount);
    }

    //查询资金交易流水 接受操作员代码 查询资金交易流水 返回资金交易流水详细信息
    @GetMapping("/selectCapitalTradeRecord/{operatorCode}")
    public Result SelectCapitalTradeRecord(@PathVariable String operatorCode) {
        List<CapitalTradeRecord> capitalTradeRecords = capitalService.selectCapitalTradeRecord(operatorCode);
        return Result.ok(capitalTradeRecords);
    }

    //查询出入金流水 接受操作员代码 查询出入金流水  返回出入金流水详细信息
    @GetMapping("/selectDepositAndWithdrawalRecord/{operatorCode}")
    public Result SelectDepositAndWithdrawalRecord(@PathVariable String operatorCode) {
        List<DepositAndWithdrawalRecord> depositAndWithdrawalRecords = capitalService.selectDepositAndWithdrawalRecord(operatorCode);
        return Result.ok(depositAndWithdrawalRecords);
    }

    //查询出入金申请流水 接受操作员代码 查询出入金申请流水 返回出入金申请流水详细信息
    @GetMapping("/selectDepositAndWithdrawalRequestRecord/{operatorCode}")
    public Result SelectDepositAndWithdrawalRequestRecord(@PathVariable String operatorCode) {
        List<DepositAndWithdrawalRequestRecord> depositAndWithdrawalRequestRecords = capitalService.selectDepositAndWithdrawalRequestRecord(operatorCode);
        return Result.ok(depositAndWithdrawalRequestRecords);
    }
}