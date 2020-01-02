package com.jxnu.finance;

import com.jxnu.finance.crawler.component.ReportDownLoadService;
import com.jxnu.finance.crawler.strategy.multiFundNetWorth.BaseMultiNetWorthStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by coder on 2017/11/11.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath*:spring/*.xml")
public class CrontabStrategyTest
{
    @Autowired
    private BaseMultiNetWorthStrategy multiNetWorthStrategy;
    @Autowired
    private ReportDownLoadService reportDownLoadService;

    @Test
    public void handlerTest() {
        multiNetWorthStrategy.handler();
    }

    @Test
    public void download(){
        reportDownLoadService.download("600438");
    }


}
