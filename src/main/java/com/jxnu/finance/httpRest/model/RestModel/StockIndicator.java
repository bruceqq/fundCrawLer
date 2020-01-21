package com.jxnu.finance.httpRest.model.RestModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yaphyao
 * @version 2018/2/28
 * @see com.jxnu.finance.httpRest.model.RestModel
 */
@Setter
@Getter
@ToString
public class StockIndicator {
    private String totalMarketValue;   //总市值
    private String netWorth;           //净资产
    private String netProfit;          //净利润
    private String grossProfitMargin;  //毛利率
    private String netInterestRate;    //净利率
    private String pe;                 //市盈率
    private String pb;                 //市净率
    private String roe;                //ROE
    private String subject;            //行业
    private String price;              //当前股价
    private int quarter;               //季度
}
