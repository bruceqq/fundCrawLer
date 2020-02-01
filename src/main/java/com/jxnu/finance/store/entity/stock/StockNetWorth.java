package com.jxnu.finance.store.entity.stock;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class StockNetWorth {
    private String stockCode;
    private String stockName;
    private String time;
    private Double stockNetWorth;
    private Date modifyTime;
}
