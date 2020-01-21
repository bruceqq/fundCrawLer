package com.jxnu.finance.crawler.strategy.singleFundNetWorth;


import com.jxnu.finance.store.entity.fund.Fund;
import com.jxnu.finance.store.entity.fund.FundNetWorth;
import com.jxnu.finance.store.entity.fund.FundStock;
import com.jxnu.finance.store.entity.stock.StockExtra;
import com.jxnu.finance.store.mapper.FundStockStore;
import com.jxnu.finance.store.mapper.StockExtraStore;
import com.jxnu.finance.utils.TimeUtil;
import com.jxnu.finance.utils.base.PopBeanUtils;
import com.jxnu.finance.utils.parse.StockParseUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service("stockStrategy")
public class StockStrategy extends BaseSingleNetWorthStrategy {
    @Autowired
    private FundStockStore stockStore;
    @Autowired
    private StockExtraStore stockExtraStore;
    @Value("${tiantian.jjcc}")
    private String url;
    @Value("${tiantain.pe}")
    private String sylUrl;
    @Value("${tiantian.stockUrl}")
    private String stockUrl;
    @Autowired
    private StockExtraStrategy stockExtraStrategy;

    @PostConstruct
    public void init() {
        super.next = stockExtraStrategy;
    }

    @Override
    public void handler(List<FundNetWorth> fundNetWorthList, Fund fund) {
        String fundCode = "";
        if (fund == null || StringUtils.isBlank(fundCode = fund.getCode())) return;
        List<String> times = TimeUtil.latestYear(5);
        for (String time : times) {
            String newUrl = "";
            newUrl = url.replace("#", fundCode).replace("@", time).replace("$", String.valueOf(new Random(5).nextInt()));
            List<FundStock> stocks = StockParseUtils.parseStock(newUrl, fundCode, time, stockUrl);
            if (stocks.isEmpty()) continue;
            stockStore.insert(stocks);
            List<StockExtra> stockExtras = new ArrayList<StockExtra>();
            for (FundStock fundStock : stocks) {
                StockExtra stockExtra = PopBeanUtils.copyProperties(fundStock, StockExtra.class);
                stockExtras.add(stockExtra);
            }
            if (stockExtras.isEmpty()) continue;
            stockExtraStore.insert(stockExtras);
        }
        if (super.next != null) {
            super.handler(fundNetWorthList, fund);
        }
    }
}
