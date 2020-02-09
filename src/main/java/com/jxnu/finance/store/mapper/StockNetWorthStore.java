package com.jxnu.finance.store.mapper;

import com.jxnu.finance.store.entity.stock.StockNetWorth;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author shoumiao_yao
 * @date 2016-07-01
 */
@Component
public class StockNetWorthStore extends BaseStore<StockNetWorth> {

    @PostConstruct
    public void init() {
        super.storeName = "stockNetWorth";
    }

    /**
     * 查询某段时间内的股票净值最大值
     *
     * @param map
     * @return
     */
    public Double selectMaxNetWorth(Map<String, String> map) {
        return super.template.selectOne(super.storeName + ".selectMaxNetWorth", map);
    }

}
