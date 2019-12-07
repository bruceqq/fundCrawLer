package com.jxnu.finance.store.entity.strategy;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class StrategyCrontabSell {
    private Integer id;
    private Integer crontabId;
    private String time;
    private Float share;
    private Float netWorth;
    private Float amount;
    private Float rate;
    private String endTime;
}
