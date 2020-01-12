package com.jxnu.finance.store.mapper;

import com.jxnu.finance.store.entity.stock.StockPosition;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author shoumiao_yao
 * @date 2016-07-01
 */
@Component
public class StockPositionStore extends BaseStore<StockPosition> {

    @PostConstruct
    public void init() {
        super.storeName = "stockPosition";
    }



}
