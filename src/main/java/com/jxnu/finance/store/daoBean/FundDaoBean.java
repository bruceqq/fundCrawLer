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
public class FundDaoBean {
    private String handler;
    private String code;

    public FundDaoBean() {
    }

    public FundDaoBean(String handler) {
        this.handler = handler;
    }

    public FundDaoBean(String handler, String code) {
        this.handler = handler;
        this.code = code;
    }

}
