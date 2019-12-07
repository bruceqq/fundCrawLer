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
public class PurchaseAnalyze {
    private Integer crontabId;
    private Integer fundCode;
    private String fundName;
    private Float amountSum;
    private Float shareSum;
    private Integer num;
}
