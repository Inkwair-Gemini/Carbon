package com.carbon.controller;

import com.carbon.input.CapitalAndQuota.QuotaTransferPost;
import com.carbon.po.Quota.ClientRegisterQuota;
import com.carbon.po.Quota.ClientTradeQuota;
import com.carbon.po.Quota.QuotaTradeRecord;
import com.carbon.po.Quota.QuotaTransferRecord;
import com.carbon.result.Result;
import com.carbon.service.QuotaService;
import com.carbon.utils.ClientIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.carbon.utils.toolUtils.TimeUtils.getCurrentTimestamp;

@RestController
@RequestMapping("/quota")
public class QuotaController {
    @Autowired
    QuotaService quotaService;
    @Autowired
    ClientIdUtils clientIdUtils;

    //配额转入/转出
    @PostMapping("/transfer")
    public Result  QuotaTransfer(@RequestBody QuotaTransferPost quotaTransferPost, @RequestBody String operatorCode){
        try {
            String quotaAccount = quotaTransferPost.getQuotaAccount();
            String subjectMatterCode = quotaTransferPost.getSubjectMatterCode();
            String type = quotaTransferPost.getType();
            Double actualAmount = quotaTransferPost.getActualAmount();
            String clientId = clientIdUtils.getIdByQuotaAccountId(quotaAccount);
            if (type.equals("转入")) {
                quotaService.QuotaIn(quotaAccount, subjectMatterCode, actualAmount);
            }else if (type.equals("转出")) {
                quotaService.QuotaOut(quotaAccount, subjectMatterCode, actualAmount);
            }
            QuotaTransferRecord quotaTransferRecord = new QuotaTransferRecord(
                    null,
                    getCurrentTimestamp(),
                    clientIdUtils.getNameByQuotaAccountId(quotaAccount),
                    operatorCode,
                    type,
                    subjectMatterCode,
                    clientId,
                    quotaTransferPost.getSubjectMatterName(),
                    actualAmount,
                    quotaService.SelectClientTradeQuota(clientId,subjectMatterCode).get(0).getAmount(),
                    quotaService.SelectClientTradeQuota(clientId,subjectMatterCode).get(0).getAmount()-(type.equals("转入")?actualAmount:-actualAmount),
                    quotaAccount,
                    type.equals("转入")?"已转入":"已转出",
                    type.equals("转入")?"转入配额:":"转出配额:"+actualAmount
            );
            quotaService.addQuotaTransferRecord(quotaTransferRecord);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
    //查询交易配额
    @GetMapping("/selectTradeQuota/{clientId}")
    public Result SelectTradeQuota(@PathVariable String clientId){
        List<ClientTradeQuota> clientTradeQuotas = quotaService.SelectClientTradeQuota(clientId,"");
        return Result.ok(clientTradeQuotas);
    }
    //查询登记配额
    @GetMapping("/selectRegisterQuota/{clientId}")
    public Result SelectRegisterQuota(@PathVariable String clientId){
        List<ClientRegisterQuota> clientRegisterQuotas = quotaService.SelectClientRegisterQuota(clientId, "");
        return Result.ok(clientRegisterQuotas);
    }
    //查询转入转出配额记录
    @GetMapping("/selectInOrOutQuotaRecord/{clientId}")
    public Result SelectInOrOutQuotaRecord(@PathVariable String clientId){
        List<QuotaTransferRecord> quotaTransferRecords = quotaService.SelectTransferRecord(clientId);
        return Result.ok(quotaTransferRecords);
    }
    //查询配额交易记录
    @GetMapping("/selectQuotaTradeRecord/{clientId}")
    public Result SelectQuotaTradeRecord(@PathVariable String clientId){
        List<QuotaTradeRecord> quotaTradeRecords = quotaService.selectQuotaTradeRecord(clientId);
        return Result.ok(quotaTradeRecords);
    }
}
