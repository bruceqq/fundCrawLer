package com.jxnu.finance.store.entity.fund;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by coder on 2017/10/29.
 */
@Setter
@Getter
@ToString
public class FundRank {
    private Integer id;
    private String fundCode;
    private Float netWorth;
    private Float ratio;
    private String time;
}
