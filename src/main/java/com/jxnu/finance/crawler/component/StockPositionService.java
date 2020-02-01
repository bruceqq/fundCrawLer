package com.jxnu.finance.crawler.component;


import com.jxnu.finance.config.CrawlerConfig;
import com.jxnu.finance.store.entity.stock.StockExtra;
import com.jxnu.finance.store.entity.stock.StockPosition;
import com.jxnu.finance.store.mapper.StockExtraStore;
import com.jxnu.finance.store.mapper.StockPositionStore;
import com.jxnu.finance.utils.parse.StockParseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 机构最新持仓
 */
@Component
public class StockPositionService {
    private static final List<String> keywords = new ArrayList<String>();

    @PostConstruct
    public void init() {
        keywords.add("社保");
        keywords.add("高毅");
        keywords.add("高瓴");
        keywords.add("中央");
        keywords.add("香港");
        keywords.add("股通");
        keywords.add("冯柳");
    }

    @Autowired
    private StockPositionStore stockPositionStore;
    @Autowired
    private StockExtraStore stockExtraStore;
    @Autowired
    private CrawlerConfig crawlerConfig;

    @Async
    public void parse(String stockCode) {
        String stockName = "";
        StockExtra stockExtra = stockExtraStore.selectOne(stockCode);
        if (stockExtra != null) {
            stockName = stockExtra.getStockName();
        }
        List<StockPosition> stockPositions = StockParseUtils.institutionalPosition(crawlerConfig.getTiantianStockholder(), stockCode, stockName, keywords);
        if(CollectionUtils.isEmpty(stockPositions)){
            return;
        }
        stockPositionStore.insert(stockPositions);
    }


}
