package com.jxnu.finance.store.entity.strategy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by coder on 2017/11/11.
 */
@Setter
@Getter
@ToString
public class StrategyPurchase {
    private Integer id;
    private Integer crontabId;
    private Integer fundCode;
    private String fundName;
    private String time;
    private float netWorth;
    private float share;
    private float amount;
    private Date createTime;
    private Date updateTime;
}
