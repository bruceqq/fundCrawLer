package com.jxnu.finance.store.entity.fund;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 基金
 *
 * @author shoumiao_yao
 */
@Setter
@Getter
@ToString
public class Fund {
    private String code;
    private String name;
    private String handler;
    private String type;
    private String companyCode;
    private String companyName;
}
