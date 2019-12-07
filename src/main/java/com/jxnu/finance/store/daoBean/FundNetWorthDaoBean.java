package com.jxnu.finance.store.daoBean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yaphyao
 * @version 2018/7/13
 * @see com.jxnu.finance.store.daoBean
 */
@Setter
@Getter
@ToString
public class FundNetWorthDaoBean {
    private String fundCode;
    private String time;

    public FundNetWorthDaoBean() {
    }

    public FundNetWorthDaoBean(String fundCode) {
        this.fundCode = fundCode;
    }

    public FundNetWorthDaoBean(String fundCode, String time) {
        this.fundCode = fundCode;
        this.time = time;
    }
}
