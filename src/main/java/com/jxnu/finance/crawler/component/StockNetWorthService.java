package com.jxnu.finance.crawler.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jxnu.finance.store.entity.stock.StockExtra;
import com.jxnu.finance.store.entity.stock.StockNetWorth;
import com.jxnu.finance.store.entity.strategy.Mail;
import com.jxnu.finance.store.mapper.MailStore;
import com.jxnu.finance.store.mapper.StockExtraStore;
import com.jxnu.finance.store.mapper.StockNetWorthStore;
import com.jxnu.finance.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class StockNetWorthService {
    private static final String url = "http://www.cninfo.com.cn/data/project/commonInterface";
    @Autowired
    private StockNetWorthStore stockNetWorthStore;
    @Autowired
    private StockExtraStore stockExtraStore;
    @Autowired
    private MailStore mailStore;

    public void netWorth(String stockCode) {
        if (StringUtil.isBank(stockCode)) {
            return;
        }
        StockExtra stockExtra = stockExtraStore.selectOne(stockCode);
        if (stockExtra == null) {
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("mergerMark", "sysapi1072");
        params.put("paramStr", "scode=" + stockCode);
        String response = OkHttpUtils.post(params, new HashMap<String, String>(), url);
        if (StringUtil.isBank(response)) {
            return;
        }
        JSONArray jsonArray = JSONArray.parseArray(response);
        if (jsonArray == null || jsonArray.isEmpty()) {
            return;
        }

        List<StockNetWorth> stockNetWorths = new ArrayList<StockNetWorth>();
        JSONObject lastNetWorthJson = jsonArray.getJSONObject(0);
        mail(stockExtra, lastNetWorthJson);
        for (Object object : jsonArray) {
            StockNetWorth stockNetWorth = new StockNetWorth();
            JSONObject jsonObject = (JSONObject) object;
            stockNetWorth.setStockCode(stockCode);
            stockNetWorth.setTime(jsonObject.getString("TRADEDATE"));
            stockNetWorth.setStockNetWorth(jsonObject.getDoubleValue("F002N"));
            stockNetWorth.setStockName(stockExtra.getStockName());
            stockNetWorths.add(stockNetWorth);
        }
        stockNetWorthStore.insert(stockNetWorths);
    }

    /**
     * 计算3个月内净值下降30%的股票
     *
     * @param stockExtra
     * @param lastNetWorthJson
     */
    public void mail(StockExtra stockExtra, JSONObject lastNetWorthJson) {
        String stockCode = stockExtra.getStockCode();
        /**
         * 邮件次数控制
         */
        Integer count = mailStore.queryMail(stockCode, "1");
        if (count > 0) {
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String endTime = TimeUtil.current(simpleDateFormat);
        String startTime = TimeUtil.current(simpleDateFormat, Calendar.DAY_OF_YEAR, -90);
        Map<String, String> params = new HashMap<String, String>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("stockCode", stockCode);
        Double maxNetWorth = stockNetWorthStore.selectMaxNetWorth(params);
        if (maxNetWorth != null && lastNetWorthJson != null) {
            Double lastNetWorth = lastNetWorthJson.getDoubleValue("F002N");
            Double divide = CalculateUtil.divide(maxNetWorth - lastNetWorth, maxNetWorth, 2);
            if (divide > 0.3) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("<html><head></head><body>");
                stringBuffer.append(stockExtra.getStockName() + ": <a href=\"" + stockExtra.getStockUrl() + "\">" + "下降比例:" + divide);
                stringBuffer.append("</body></html>");
                MailUtil.sendmail(stockExtra.getStockName() + "净值下降通知", stringBuffer.toString());
                /**
                 * 记录发送的邮件
                 */
                Mail mail = new Mail();
                mail.setCode(stockCode);
                mail.setTime(endTime);
                mail.setType("1");
                mailStore.insert(Lists.newArrayList(mail));
            }
        }
    }
}
