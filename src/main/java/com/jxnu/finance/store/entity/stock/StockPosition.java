package com.jxnu.finance.store.entity.stock;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class StockPosition {
     private String institutional;
     private String time;
     private String stockCode;
     private String stockName;
     private Double ratio;
     private Date createTime;
     private Date modifyTime;
     private String shareChange;

}
