package com.jxnu.finance.crawler.component;


import com.google.common.collect.Lists;
import com.jxnu.finance.store.entity.stock.StockExtra;
import com.jxnu.finance.store.entity.stock.StockMark;
import com.jxnu.finance.store.mapper.StockMarkStore;
import com.jxnu.finance.utils.NumberUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class StockFutureService {
    @Autowired
    private StockMarkStore stockMarkStore;


    @Async
    public void future(StockExtra stockExtra) {
        if (stockExtra == null) {
            return;
        }
        /**
         * 股票价格
         */
        StockMark stockMark = new StockMark();
        BeanUtils.copyProperties(stockExtra, stockMark);
        if (StringUtils.isBlank(stockExtra.getPrice())
            || StringUtils.isBlank(stockExtra.getTotalShare())
            || stockExtra.getNetProfit() != null
            || stockExtra.getQuarter() == null
            || StringUtils.isBlank(stockExtra.getRoe())) {
            return;
        }
        Double roe = Double.parseDouble(stockExtra.getRoe());
        Double price = Double.parseDouble(stockExtra.getPrice());
        int quarter = stockExtra.getQuarter();
        double totalTotalShare = Double.parseDouble(stockExtra.getTotalShare());
        /**
         * 预计全年的ROE
         */
        Double estimateRoe = NumberUtil.multiply(roe, NumberUtil.divide(4.0D, quarter + 0D));
        /**
         * 预计全年的利润
         */
        Double estimateNetProfit = NumberUtil.multiply(stockExtra.getNetProfit(), NumberUtil.divide(4.0D, quarter + 0D));
        /**
         * 预计三年后的利润
         */
        Double futureNetProfit1 = NumberUtil.multiply(estimateNetProfit, 1.1D);
        Double futureNetProfit2 = NumberUtil.multiply(futureNetProfit1, 1.1D);
        Double futureNetProfit = NumberUtil.multiply(futureNetProfit2, 1.1D);
        /**
         * 预计三年后的股票价格
         */
        Double futurePrice = NumberUtil.divide(NumberUtil.multiply(futureNetProfit, 15D), totalTotalShare);
        stockMark.setFuturePrice(futurePrice.toString());
        Double ratio = NumberUtil.divide(futurePrice, price);
        /**
         * 价格翻倍
         */
        if (ratio > 1.7 && estimateRoe > 10) {
            stockMarkStore.insert(Lists.newArrayList(stockMark));
        }
    }
}
