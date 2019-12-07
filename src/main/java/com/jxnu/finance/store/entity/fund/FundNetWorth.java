package com.jxnu.finance.store.entity.fund;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by coder on 2016/7/2.
 */
@Setter
@Getter
@ToString
public class FundNetWorth {
    private String fundCode;
    private String time;
    private Float netWorth;
    private Float rate;
}
