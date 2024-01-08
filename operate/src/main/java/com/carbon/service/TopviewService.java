package com.carbon.service;

import com.carbon.output.DealDivision;
import com.carbon.output.TimeDivision;

import java.util.List;

public interface TopviewService {
    public TimeDivision getTimeDivision();
    public List<DealDivision> getDealDivision();
}
