package com.carbon.output;

import com.carbon.po.CapitalAccount;
import com.carbon.po.ClientTradeQuota;
import com.carbon.po.QuotaAccount;
import com.carbon.po.QuotaTradeRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectPositionInfoResult implements Serializable {
    private CapitalAccount capitalAccount;
    private QuotaAccount quotaAccount;
    private List<ClientTradeQuota> quotaList;
}
