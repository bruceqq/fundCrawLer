package com.jxnu.finance.store.mapper;

import com.jxnu.finance.store.entity.stock.StockExtra;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shoumiao_yao
 * @date 2016-07-01
 */
@Component
public class StockExtraStore extends BaseStore<StockExtra> {

    @PostConstruct
    public void init() {
        super.storeName = "stockExtra";
    }


    public StockExtra selectOne(String stockCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stockCode", stockCode);
        return selectOne(map);
    }


}
