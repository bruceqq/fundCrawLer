package com.jxnu.finance.crawler.grabThread.specific;

import com.jxnu.finance.store.entity.fund.Fund;
import com.jxnu.finance.store.entity.fund.FundCompany;
import com.jxnu.finance.store.mapper.CompanyStore;
import com.jxnu.finance.store.mapper.FundStore;
import com.jxnu.finance.utils.parse.ParseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author shoumiao_yao
 * @date 2016-07-01
 */
@Component
public class FundGrab extends Grab {
    private final static Logger logger = LoggerFactory.getLogger(FundGrab.class);
    @Autowired
    private FundStore fundStore;
    @Autowired
    private CompanyStore companyStore;
    @Value("${tiantian.finance}")
    private String fundUrl;

    public void handler(Integer num) {
        List<FundCompany> companyList = companyStore.selectMulti(new HashMap());
        for (FundCompany company : companyList) {
            try {
                List<Fund> fundList = ParseUtils.parseFund(this.fundUrl, company);
                List<Fund> newFundList = new ArrayList<Fund>();
                for (Fund fund : fundList) {
                    String type = fund.getType();
                    if (StringUtils.equals(type,"混合型")
                        || StringUtils.equals(type,"股票指数")
                        || StringUtils.equals(type,"分级杠杆")
                        || StringUtils.equals(type,"ETF-场内")
                        ){
                        newFundList.add(fund);
                    }
                }
                if (!newFundList.isEmpty()) {
                    fundStore.insert(newFundList);
                }
            } catch (Exception e) {
                logger.error("error:{}", ExceptionUtils.getMessage(e));
            }
        }
    }
}
