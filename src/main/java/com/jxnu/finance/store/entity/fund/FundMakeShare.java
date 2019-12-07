package com.jxnu.finance.store.entity.fund;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by coder on 2/04/17.
 */
@Setter
@Getter
@ToString
public class FundMakeShare {
    private String code;
    private String name;
    private Integer total;
}
