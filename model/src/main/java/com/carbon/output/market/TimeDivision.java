package com.carbon.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
//分时
public class TimeDivision implements Serializable {
    private Timestamp time;
    private Double price;
    private Double averagePrice;
}
