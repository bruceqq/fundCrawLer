package com.jxnu.finance.store.entity.fund;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FundStock {
    private String stockCode;
    private String stockName;
    private String fundCode;
    private String time;
    private String stockUrl;
    private String pe;
    private String pb;
    private String totalMarketValue;   //总市值
    private String netWorth;           //净资产
    private String netProfit;          //净利润
    private String grossProfitMargin;  //毛利率
    private String netInterestRate;    //净利率
    private String roe;                //ROE
    private String subject;            //行业
    private String price;              //股价
    private String totalShare;         //总份额
    private String shareOut;           //分红
}
