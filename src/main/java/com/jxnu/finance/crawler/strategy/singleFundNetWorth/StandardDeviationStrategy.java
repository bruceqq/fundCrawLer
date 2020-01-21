package com.jxnu.finance.crawler.strategy.singleFundNetWorth;


import com.jxnu.finance.store.entity.fund.Fund;
import com.jxnu.finance.store.entity.fund.FundNetWorth;
import com.jxnu.finance.store.mapper.FundNetWorthStore;
import com.jxnu.finance.store.mapper.FundShareOutStore;
import com.jxnu.finance.store.mapper.FundStore;
import com.jxnu.finance.store.mapper.StrategyCrontabSellStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Service("standardDeviationStrategy")
public class StandardDeviationStrategy extends BaseSingleNetWorthStrategy {
    @Autowired
    private FundNetWorthStore netWorthStore;
    @Autowired
    private StrategyCrontabSellStore crontabSellStore;
    @Autowired
    private FundStore fundStore;
    @Autowired
    private FundShareOutStore fundShareOutStore;
    @Resource(name = "stockExtraStrategy")
    private BaseSingleNetWorthStrategy stockStrategy;

    @PostConstruct
    public void init() {
        super.next = stockStrategy;
    }

    @Override
    public void handler(List<FundNetWorth> fundNetWorthList, Fund fund) {
        if (super.next != null) {
            super.next.handler(fundNetWorthList, fund);
        }
    }
}
