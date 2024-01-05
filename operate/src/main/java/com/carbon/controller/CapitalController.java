package com.carbon.controller;

import com.carbon.input.CapitalTransferPost;
import com.carbon.po.*;
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

    //资金转入/转出
    @PostMapping("/transfer")
    public Result  QuotaTransfer(@RequestBody CapitalTransferPost capitalTransferPost){
        // todo 事务管理
        try {
            String type = capitalTransferPost.getType();
            if (type.equals("转入"))
                capitalService.capitalIn(capitalTransferPost,);
            else if (type.equals("转出"))
                capitalService.capitalOut(capitalTransferPost,);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
    //查询资金账号
    @GetMapping("/selectCapitalAccount/{accountId}")
    public Result SelectCapitalAccount(@PathVariable String accountId){
        CapitalAccount capitalAccount = capitalService.selectCapitalAccount(accountId);
        return Result.ok(capitalAccount);
    }
    //查询资金交易流水
    @GetMapping("/selectCapitalTradeRecord/{operatorCode}")
    public Result SelectCapitalTradeRecord(@PathVariable String operatorCode){
        List<CapitalTradeRecord> capitalTradeRecords=capitalService.selectCapitalTradeRecord(operatorCode);
        return Result.ok(capitalTradeRecords);
    }
    //查询出入金流水
    @GetMapping("/selectDepositAndWithdrawalRecord/{operatorCode}")
    public Result SelectDepositAndWithdrawalRecord(@PathVariable String operatorCode){
        List<DepositAndWithdrawalRecord> depositAndWithdrawalRecords = capitalService.selectDepositAndWithdrawalRecord(operatorCode);
        return Result.ok(depositAndWithdrawalRecords);
    }
    //查询出入金申请流水
    @GetMapping("/selectDepositAndWithdrawalRequestRecord/{operatorCode}")
    public Result SelectDepositAndWithdrawalRequestRecord(@PathVariable String operatorCode){
        List<DepositAndWithdrawalRequestRecord> depositAndWithdrawalRequestRecords = capitalService.selectDepositAndWithdrawalRequestRecord(operatorCode);
        return Result.ok(depositAndWithdrawalRequestRecords);
    }
}