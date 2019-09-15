package com.jxnu.finance.crawler.grabThread.specific;

import com.jxnu.finance.crawler.strategy.BeforeHandlerFundNetWorth.BeforeHandlerNetWorthStrategy;
import com.jxnu.finance.crawler.strategy.multiFundNetWorth.BaseMultiNetWorthStrategy;
import com.jxnu.finance.crawler.strategy.singleFundNetWorth.BaseSingleNetWorthStrategy;
import com.jxnu.finance.store.entity.fund.Fund;
import com.jxnu.finance.store.entity.fund.FundNetWorth;
import com.jxnu.finance.store.entity.fund.FundShareOut;
import com.jxnu.finance.store.mapper.FundNetWorthStore;
import com.jxnu.finance.store.mapper.FundShareOutStore;
import com.jxnu.finance.store.mapper.FundStore;
import com.jxnu.finance.utils.parse.ParseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by coder on 2016/7/2.
 */
@Component
public class FundNetWorthGrab extends Grab {
    private final static Logger logger = LoggerFactory.getLogger(FundNetWorthGrab.class);
    @Value("${tiantian.fundNetWorth}")
    private String fundNetWorthUrl;
    @Value("${tiantian.fundUrl}")
    private String fundUrl;
    @Autowired
    private FundStore fundStore;
    @Autowired
    private FundNetWorthStore fundNetWorthStore;
    @Autowired
    private FundShareOutStore fundShareOutStore;
    @Resource(name = "stockStrategy")
    private BaseSingleNetWorthStrategy fundNetWorthStrategy;
    @Resource(name = "multiNetWorthRankStrategy")
    private BaseMultiNetWorthStrategy multiNetWorthStrategy;
    @Resource(name = "rankBeforeHandlerStrategy")
    private BeforeHandlerNetWorthStrategy beforeHandlerNetWorthStrategy;

    public void handler(Integer num) {
        List<Fund> fundList = fundStore.selectMulti("");
        if (num != -1) {
            /**
             * 基金净值获取前 策略执行
             */
            //beforeHandlerNetWorthStrategy.handler();
            List<FundNetWorth> fundNetWorthList = new ArrayList<FundNetWorth>();
            for (Fund fund : fundList) {
                try {
                    /**
                     * 基金净值获取
                     */
                    //fundNetWorthList = fundNetWorth(num, fund);
                    /**
                     * 单个基金净值获取后 策略执行
                     */
                    fundNetWorthStrategy.handler(fundNetWorthList, fund);
                } catch (Exception e) {
                    logger.error("error:{}", ExceptionUtils.getStackTrace(e));
                }
            }
        }
        /**
         * 所有基金净值获取后 策略执行
         */
        multiNetWorthStrategy.handler();
    }


    /**
     * 基金净值
     *
     * @return
     */
    private List<FundNetWorth> fundNetWorth(Integer num, Fund fund) {
        Random random = new Random(1000);
        List<FundNetWorth> fundNetWorths = new ArrayList<FundNetWorth>();
        String code = "";
        String count;
        if (fund == null || StringUtils.isEmpty(code = fund.getCode())) {
            return fundNetWorths;
        }
        if (num == 0) {
            String countUrl = this.fundNetWorthUrl.replace("$", code).replace("#", "1").replace("%", random.nextInt() + "");
            count = ParseUtils.parseFundNetWorthCount(countUrl);
        } else {
            count = num.toString();
        }
        String content = this.fundNetWorthUrl.replace("$", code).replace("#", count).replace("%", random.nextInt() + "");
        List<FundNetWorth> fundNetWorthList = ParseUtils.parseFundNetWorth(content, code);
        if (!fundNetWorthList.isEmpty()) {
            fundNetWorthStore.insert(fundNetWorthList);
        }
        insetShareOuts(code);
        return fundNetWorthList;
    }

    /**
     * 基金分红
     *
     * @param code
     */
    private void insetShareOuts(String code) {
        List<FundShareOut> fundShareOuts = new ArrayList<FundShareOut>();
        List<String> shareOuts = ParseUtils.parseFundShareOut(fundUrl.replace("#", code));
        if (shareOuts == null || shareOuts.isEmpty()) return;
        for (String shareOut : shareOuts) {
            FundShareOut share = new FundShareOut();
            share.setFundCode(code);
            share.setTime(shareOut);
            fundShareOuts.add(share);
        }
        if (shareOuts.isEmpty()) return;
        fundShareOutStore.insert(fundShareOuts);
    }
}
