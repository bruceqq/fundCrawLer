package com.jxnu.finance.store.entity.stock;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 股票解禁
 */
@Setter
@Getter
@ToString
public class StockiftBean {
    private String stockCode;
    private String liftBeanTime;
    private String liftBeanShare;
}
