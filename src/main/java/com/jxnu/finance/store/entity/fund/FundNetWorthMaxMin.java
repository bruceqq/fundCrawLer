package com.jxnu.finance.store.entity.fund;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by coder on 2017-03-19.
 */
@Setter
@Getter
@ToString
public class FundNetWorthMaxMin {
    private Float min;
    private Float max;
}
