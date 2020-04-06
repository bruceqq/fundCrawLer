package com.jxnu.finance.crawler.strategy.multiFundNetWorth;


import com.jxnu.finance.crawler.component.ReportDownLoadService;
import com.jxnu.finance.crawler.component.StockFutureService;
import com.jxnu.finance.crawler.component.StockNetWorthService;
import com.jxnu.finance.crawler.component.StockPositionService;
import com.jxnu.finance.store.entity.stock.StockExtra;
import com.jxnu.finance.store.mapper.StockExtraStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

@Component("stockExtraStrategy")
public class StockExtraStrategy extends AfterHandlerNetWorthStrategy {
    @Autowired
    private StockExtraStore stockExtraStore;
    @Autowired
    private StockFutureService stockFutureService;
    /**
     * 年报
     */
    @Autowired
    private ReportDownLoadService reportDownLoadService;
    /**
     * 机构持仓
     */
    @Autowired
    private StockPositionService stockPositionService;
    /**
     * 股票净值
     */
    @Autowired
    private StockNetWorthService stockNetWorthService;

    @PostConstruct
    public void init() {
        super.next = null;
    }


    @Override
    public void handler() {
        List<StockExtra> stockExtras = stockExtraStore.selectMulti(new HashMap());
        for (StockExtra stockExtra : stockExtras) {
            String stockCode = stockExtra.getStockCode();
            try {
                stockNetWorthService.netWorth(stockCode);
            } catch (Exception e) {
            }
            reportDownLoadService.download(stockCode);
            //stockPositionService.parse(stockCode);
            //stockFutureService.future(stockExtra);
        }
    }
}
