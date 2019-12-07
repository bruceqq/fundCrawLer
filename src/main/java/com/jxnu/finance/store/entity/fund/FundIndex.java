package com.jxnu.finance.store.entity.fund;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by coder on 4/4/17.
 */
@Setter
@Getter
@ToString
public class FundIndex {
    private String code;              //代码
    private String time;               //时间
    private String name;               //名称
    private Float latestPrice;         //最新价
    private Float changeAmount;        //涨跌额
    private Float ratio;               //比例
    private Float turnover;            //成交额
    private Float volume;              //成交量
    private Float yesterday;           //昨收
    private Float today;               //今收
    private Float max;                 //最高
    private Float min;                 //最低
}
