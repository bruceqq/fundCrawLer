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
public class StrategyCrontabStoreDaoBean {
    private Integer id;
    private Integer state;
    private String startTime;
    private String endTime;
    private Float amount;
}
