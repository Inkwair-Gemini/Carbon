package com.carbon.controller;

import com.carbon.output.market.DealDivision;
import com.carbon.output.market.TimeDivision;
import com.carbon.result.Result;
import com.carbon.service.TopviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/topview")
public class TopviewController {
    @Autowired
    private TopviewService topviewService;

    @GetMapping("/timeDivision")
    public Result getTimeDivision() {
        try {
            TimeDivision timeDivision = topviewService.getTimeDivision();
            return Result.ok(timeDivision);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }

    @GetMapping("/dealDivision")
    public Result getDealDivision() {
        try {
            List<DealDivision> dealDivisionList = topviewService.getDealDivision();
            return Result.ok(dealDivisionList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }
}
