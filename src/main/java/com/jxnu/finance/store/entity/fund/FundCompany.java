package com.jxnu.finance.store.entity.fund;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FundCompany {
    private String code;         //公司代码
    private String name;          //公司名称
    private String createTime;      //公司创建时间
    private Integer fundNum;      //旗下基金数量
    private String handler;       //总经理
    private Double scale;         //规模
}
