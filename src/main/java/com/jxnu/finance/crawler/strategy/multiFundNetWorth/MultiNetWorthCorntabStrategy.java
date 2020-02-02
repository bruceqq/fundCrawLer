package com.jxnu.finance.crawler.strategy.multiFundNetWorth;

import com.jxnu.finance.store.entity.fund.FundNetWorth;
import com.jxnu.finance.store.entity.strategy.StrategyCrontab;
import com.jxnu.finance.store.entity.strategy.StrategyPurchase;
import com.jxnu.finance.store.mapper.FundNetWorthStore;
import com.jxnu.finance.store.mapper.StrategyCrontabStore;
import com.jxnu.finance.store.mapper.StrategyPurchaseStore;
import com.jxnu.finance.utils.CalculateUtil;
import com.jxnu.finance.utils.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by coder on 2017/11/11.
 */
@Component("multiNetWorthCorntabStrategy")
public class MultiNetWorthCorntabStrategy extends AfterHandlerNetWorthStrategy {
    @Autowired
    private StrategyCrontabStore strategyCrontabStore;
    @Autowired
    private FundNetWorthStore fundNetWorthStore;
    @Autowired
    private StrategyPurchaseStore purchaseStore;
    @Resource(name = "multiNetWorthAnalyzeStrategy")
    private AfterHandlerNetWorthStrategy multiNetWorthStrategy;
    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @PostConstruct
    public void init() {
        super.next = multiNetWorthStrategy;
    }

    @Override
    public void handler() {
        Map map = new HashMap();
        map.put("state", 1);
        List<StrategyCrontab> strategyCrontabList = strategyCrontabStore.selectMulti(map);
        if (strategyCrontabList.isEmpty()) return;
        for (StrategyCrontab crontab : strategyCrontabList) {
            String startTime = crontab.getStartTime();
            String endTime = crontab.getEndTime();
            Integer fundCode = crontab.getFundCode();
            Set<String> timeSet = new HashSet<String>();
            if (endTime == null || StringUtils.isBlank(endTime) || StringUtils.equals(endTime, "1")) {
                timeSet = TimeUtil.intervalTime(startTime, now());
            } else {
                timeSet = TimeUtil.intervalTime(startTime, endTime);
            }
            if (timeSet.isEmpty()) continue;
            List<StrategyPurchase> purchases = new ArrayList<StrategyPurchase>();
            for (String time : timeSet) {
                if (StringUtils.isBlank(time)) continue;
                map.clear();
                Integer count = purchaseStore.selectCount(crontab.getId(), time);
                if (count != null && count > 0) continue;
                FundNetWorth fundNetWorth = fundNetWorthStore.selectOne(String.valueOf(fundCode), time);
                if (fundNetWorth == null) continue;
                float amout = CalculateUtil.multiply(crontab.getAmount(), 1 - crontab.getBuyRate());
                StrategyPurchase purchase = new StrategyPurchase();
                purchase.setUpdateTime(new Date());
                purchase.setCreateTime(new Date());
                purchase.setCrontabId(crontab.getId());
                purchase.setAmount(amout);
                purchase.setFundCode(fundCode);
                purchase.setTime(time);
                purchase.setFundName(crontab.getFundName());
                purchase.setNetWorth(fundNetWorth.getNetWorth());
                purchase.setShare(CalculateUtil.divide(amout, fundNetWorth.getNetWorth(), 2));
                purchases.add(purchase);
            }
            if (!purchases.isEmpty()) {
                purchaseStore.insert(purchases);
            }
        }

        if (super.next != null) {
            super.next.handler();
        }
    }

    public String now() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        return dateFormat.format(calendar.getTime());
    }
}
