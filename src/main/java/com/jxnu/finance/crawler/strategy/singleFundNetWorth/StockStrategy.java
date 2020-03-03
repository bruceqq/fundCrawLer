package com.jxnu.finance.crawler.strategy.singleFundNetWorth;


import com.jxnu.finance.store.entity.fund.Fund;
import com.jxnu.finance.store.entity.fund.FundNetWorth;
import com.jxnu.finance.store.entity.fund.FundStock;
import com.jxnu.finance.store.entity.stock.StockExtra;
import com.jxnu.finance.store.mapper.FundStockStore;
import com.jxnu.finance.store.mapper.StockExtraStore;
import com.jxnu.finance.utils.StringUtil;
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
import java.util.regex.Pattern;

@Service("stockStrategy")
public class StockStrategy extends BaseSingleNetWorthStrategy {
    private static final Pattern pattern = Pattern.compile("^[0-9]\\d*$");
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

    @PostConstruct
    public void init() {
        super.next = null;
    }

    @Override
    public void handler(List<FundNetWorth> fundNetWorthList, Fund fund) {
        /**
         * 获取基金重仓股票相关信息
         */
        String fundCode = "";
        if (fund == null || StringUtils.isBlank(fundCode = fund.getCode())) return;
        List<String> times = TimeUtil.latestYear(2);
        List<StockExtra> stockExtras = new ArrayList<StockExtra>();
        for (String time : times) {
            String newUrl = "";
            newUrl = url.replace("#", fundCode).replace("@", time).replace("$", String.valueOf(new Random(5).nextInt()));
            List<FundStock> stocks = StockParseUtils.parseStock(newUrl, fundCode, time, stockUrl);
            if (stocks.isEmpty()) continue;
            stockStore.insert(stocks);
            for (FundStock fundStock : stocks) {
                StockExtra stockExtra = PopBeanUtils.copyProperties(fundStock, StockExtra.class);
                String stockCode = stockExtra.getStockCode();
                if (stockCode.contains(".SZ")) {
                    stockCode = stockCode.replaceAll(".SZ", "");
                }
                if (stockCode.contains(".SH")) {
                    stockCode = stockCode.replaceAll(".SH", "");
                }
                if (pattern.matcher(stockCode).find()
                    && stockCode.length() == 6
                    && stockExtra.getTotalMarketValue() != null
                    && !StringUtil.isBank(stockExtra.getSubject())
                    && stockExtra.getNetProfit() != null) {
                    stockExtra.setStockCode(stockCode);
                    stockExtras.add(stockExtra);
                }
            }
        }
        if (!stockExtras.isEmpty()) {
            stockExtraStore.insert(stockExtras);
        }
        /**
         * 执行下一个策略
         */
        if (super.next != null) {
            super.handler(fundNetWorthList, fund);
        }
    }
}
