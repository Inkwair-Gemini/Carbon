package com.carbon.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealDivision implements Serializable {
    private Timestamp time;
    private Double price;
    private Double amount;
}
