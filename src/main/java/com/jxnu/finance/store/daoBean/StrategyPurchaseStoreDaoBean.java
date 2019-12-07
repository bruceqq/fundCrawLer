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
public class StrategyPurchaseStoreDaoBean {
    private Integer crontabId;
    private String time;

    public StrategyPurchaseStoreDaoBean(Integer crontabId, String time) {
        this.crontabId = crontabId;
        this.time = time;
    }
}
