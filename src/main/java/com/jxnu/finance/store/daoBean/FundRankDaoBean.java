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
public class FundRankDaoBean {
    private String time;
    private Integer rate;

    public FundRankDaoBean() {
    }

    public FundRankDaoBean(String time, Integer rate) {
        this.time = time;
        this.rate = rate;
    }
}
