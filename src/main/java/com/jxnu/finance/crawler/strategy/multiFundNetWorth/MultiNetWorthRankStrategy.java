package com.jxnu.finance.crawler.strategy.multiFundNetWorth;

import com.google.common.collect.Lists;
import com.jxnu.finance.store.entity.fund.FundRank;
import com.jxnu.finance.store.mapper.FundRankStore;
import com.jxnu.finance.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by coder on 2017/11/11.
 */
@Component("multiNetWorthRankStrategy")
public class MultiNetWorthRankStrategy extends AfterHandlerNetWorthStrategy {
    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final static Integer dateNum = 20;
    @Autowired
    private FundRankStore fundRankStore;
    @Resource(name = "multiNetWorthCorntabStrategy")
    private AfterHandlerNetWorthStrategy corntabStategy;

    @PostConstruct
    public void init() {
        super.next = corntabStategy;
    }

    @Override
    public void handler() {
        //每天净值下降排行榜
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        String endTime = dateFormat.format(calendar.getTime());
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - dateNum);
        String startTime = dateFormat.format(startCalendar.getTime());
        TreeSet<String> timeSet = new TreeSet<String>(TimeUtil.intervalTime(startTime, endTime));
        int index = 0;
        List<FundRank> fundRanks = Lists.newArrayList();
        do {
            String time = timeSet.pollLast();
            fundRanks.addAll(fundRankStore.selectMulti(time));
            index++;
        } while (index < dateNum);
        fundRankStore.insert(fundRanks);
        if (super.next != null) {
            super.next.handler();
        }
    }
}
