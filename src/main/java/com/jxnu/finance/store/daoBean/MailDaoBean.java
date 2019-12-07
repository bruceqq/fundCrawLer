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
public class MailDaoBean {
    private String fundCode;
    private String type;

    public MailDaoBean() {
    }


    public MailDaoBean(String fundCode, String type) {
        this.fundCode = fundCode;
        this.type = type;
    }

}
