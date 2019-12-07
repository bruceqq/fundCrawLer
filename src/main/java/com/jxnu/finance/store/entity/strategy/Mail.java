package com.jxnu.finance.store.entity.strategy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author shoumiao_yao
 * @date 2016-10-13
 */
@Setter
@Getter
@ToString
public class Mail {
    private String id;
    private String code;
    private String time;
    private String type;
}
