package com.jxnu.finance.crawler.strategy.singleFundNetWorth;


import com.jxnu.finance.crawler.component.ReportDownLoadService;
import com.jxnu.finance.crawler.component.StockFutureService;
import com.jxnu.finance.crawler.component.StockNetWorthService;
import com.jxnu.finance.crawler.component.StockPositionService;
import com.jxnu.finance.store.entity.fund.Fund;
import com.jxnu.finance.store.entity.fund.FundNetWorth;
import com.jxnu.finance.store.entity.stock.StockExtra;
import com.jxnu.finance.store.mapper.StockExtraStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Component("stockExtraStrategy")
public class StockExtraStrategy extends BaseSingleNetWorthStrategy {
    @Autowired
    private StockExtraStore stockExtraStore;
    @Resource(name = "stockStrategy")
    private BaseSingleNetWorthStrategy stockStrategy;
    @Autowired
    private StockFutureService stockFutureService;


    @PostConstruct
    public void init() {
        super.next = stockStrategy;
    }

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


    @Override
    @Async
    public void handler(List<FundNetWorth> fundNetWorthList, Fund fund) {
        if (super.next != null) {
            super.next.handler(fundNetWorthList, fund);
        }
        List<StockExtra> stockExtras = stockExtraStore.selectMulti(new HashMap());
        for (StockExtra stockExtra : stockExtras) {
            String stockCode = stockExtra.getStockCode();
            stockNetWorthService.netWorth(stockCode);
            reportDownLoadService.download(stockCode);
            stockPositionService.parse(stockCode);
            stockFutureService.future(stockExtra);
        }
    }
}
