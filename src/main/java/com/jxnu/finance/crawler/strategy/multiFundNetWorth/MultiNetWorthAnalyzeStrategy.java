package com.jxnu.finance.crawler.strategy.multiFundNetWorth;

import com.jxnu.finance.store.entity.fund.FundNetWorth;
import com.jxnu.finance.store.entity.strategy.PurchaseAnalyze;
import com.jxnu.finance.store.entity.strategy.StrategyCrontabAnalyze;
import com.jxnu.finance.store.mapper.FundNetWorthStore;
import com.jxnu.finance.store.mapper.StrategyCrontabAnalyzeStore;
import com.jxnu.finance.store.mapper.StrategyPurchaseStore;
import com.jxnu.finance.utils.CalculateUtil;
import com.jxnu.finance.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 分析购买基金总和
 * Created by coder on 2017/11/11.
 */
@Component("multiNetWorthAnalyzeStrategy")
public class MultiNetWorthAnalyzeStrategy extends AfterHandlerNetWorthStrategy {
    private final static Logger logger = LoggerFactory.getLogger(MultiNetWorthAnalyzeStrategy.class);
    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private FundNetWorthStore fundNetWorthStore;
    @Autowired
    private StrategyPurchaseStore purchaseStore;
    @Autowired
    private StrategyCrontabAnalyzeStore analyzeStore;

    @PostConstruct
    public void init() {
        super.next = null;
    }

    @Override
    public void handler() {
        List<PurchaseAnalyze> purchaseAnalyzeList = purchaseStore.purchaseAnalyze();
        if (purchaseAnalyzeList == null || purchaseAnalyzeList.isEmpty()) return;
        List<StrategyCrontabAnalyze> strategyPurchases = new ArrayList<StrategyCrontabAnalyze>();
        for (PurchaseAnalyze purchaseAnalyze : purchaseAnalyzeList) {
            if (purchaseAnalyze == null) continue;
            //买入计算
            StrategyCrontabAnalyze strategyCrontabAnalyze = new StrategyCrontabAnalyze();
            float amount = purchaseAnalyze.getAmountSum();
            amount = new BigDecimal(String.valueOf(amount)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            float share = purchaseAnalyze.getShareSum();
            share = new BigDecimal(String.valueOf(share)).setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
            float averNetWorth = CalculateUtil.divide(amount, share, 4);
            //分析结果
            strategyCrontabAnalyze.setFundCode(purchaseAnalyze.getFundCode());
            strategyCrontabAnalyze.setCrontabAmount(amount);
            strategyCrontabAnalyze.setCrontabShare(share);
            strategyCrontabAnalyze.setAverNetWorth(averNetWorth);
            strategyCrontabAnalyze.setCrontabNum(purchaseAnalyze.getNum());
            strategyCrontabAnalyze.setCrontabId(purchaseAnalyze.getCrontabId());
            Float nowNetWorth = maxTime(purchaseAnalyze.getFundCode());
            Float rate = CalculateUtil.divide(nowNetWorth - averNetWorth, averNetWorth, 4);
            strategyCrontabAnalyze.setNetWorth(nowNetWorth);
            strategyCrontabAnalyze.setRate(rate);
            strategyCrontabAnalyze.setFundName(purchaseAnalyze.getFundName());
            strategyPurchases.add(strategyCrontabAnalyze);
        }
        if (!strategyPurchases.isEmpty()) {
            analyzeStore.insert(strategyPurchases);
        }
        if (super.next != null) {
            super.handler();
        }
    }

    protected Float maxTime(Integer fundCode) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        String startTime = dateFormat.format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 16);
        String endTime = dateFormat.format(calendar.getTime());
        Set<String> timeSet = TimeUtil.intervalTime(endTime, startTime);
        TreeSet<String> treeSet = new TreeSet<String>(timeSet);
        FundNetWorth fundNetWorth = null;
        do {
            String time = treeSet.pollLast();
            fundNetWorth = fundNetWorthStore.selectOne(String.valueOf(fundCode), time);
        } while (fundNetWorth == null);
        if (fundNetWorth == null) return 0f;
        return fundNetWorth.getNetWorth();

    }
}
