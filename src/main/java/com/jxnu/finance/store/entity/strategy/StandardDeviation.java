package com.jxnu.finance.store.entity.strategy;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class StandardDeviation {
    private String fundCode;
    private Float standardDeviation;
    private Float average;
    private Float max;
    private Float min;
    private Float maxAverRate;
    private Float minAverRate;
    private Float maxRate;
    private Float minRate;
    private Integer state;
}
