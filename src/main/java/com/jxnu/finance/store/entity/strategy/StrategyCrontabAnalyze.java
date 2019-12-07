package com.jxnu.finance.store.entity.strategy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by coder on 2017/11/11.
 */
@Setter
@Getter
@ToString
public class StrategyCrontabAnalyze {
    private Integer crontabId;
    private Integer fundCode;
    private float averNetWorth;
    private float sellNetWorth;
    private float crontabAmount;
    private float crontabShare;
    private Integer crontabNum;
    private float rate;
    private float netWorth;
    private String fundName;
}
