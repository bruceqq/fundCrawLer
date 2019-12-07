package com.jxnu.finance.store.entity.strategy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by coder on 2017/11/11.
 */
@Setter
@Getter
@ToString
public class StrategyCrontab {
    private Integer id;
    private String crontabName;
    private Integer fundCode;
    private String fundName;
    private String startTime;
    private String endTime;
    private Float amount;
    private Float buyRate;
    private Float sellRate;
    private Date createTime;
    private Date updateTime;
    private Integer state;
}
