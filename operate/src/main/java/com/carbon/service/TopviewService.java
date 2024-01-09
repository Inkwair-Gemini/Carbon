package com.carbon.service;

import com.carbon.output.market.DealDivision;
import com.carbon.output.market.TimeDivision;

import java.util.List;

public interface TopviewService {
    //todo
    public TimeDivision getTimeDivision();
    public List<DealDivision> getDealDivision();
}
