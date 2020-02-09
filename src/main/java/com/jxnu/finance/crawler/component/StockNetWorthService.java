package com.jxnu.finance.crawler.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxnu.finance.store.entity.stock.StockExtra;
import com.jxnu.finance.store.entity.stock.StockNetWorth;
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
        Double maxNetWorth = maxNetWorth(stockCode);
        List<StockNetWorth> stockNetWorths = new ArrayList<StockNetWorth>();
        JSONObject lastNetWorthJson = jsonArray.getJSONObject(0);
        if (maxNetWorth != null && lastNetWorthJson != null) {
            Double lastNetWorth = lastNetWorthJson.getDoubleValue("F002N");
            Double divide = CalculateUtil.divide(maxNetWorth - lastNetWorth, maxNetWorth, 2);
            if (divide > 0.3) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("<html><head></head><body>");
                stringBuffer.append(stockExtra.getStockName() + ": <a href=\"" + stockExtra.getStockUrl() + "\">" + "下降比例:" + divide);
                stringBuffer.append("</body></html>");
                MailUtil.sendmail(stockExtra.getStockName() + "净值下降通知", stringBuffer.toString());
            }
        }

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
     * 计算3个月内最大净值
     *
     * @param stockCode
     * @return
     */
    public Double maxNetWorth(String stockCode) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String endTime = TimeUtil.current(simpleDateFormat);
        String startTime = TimeUtil.current(simpleDateFormat, Calendar.DAY_OF_YEAR, -90);
        Map<String, String> params = new HashMap<String, String>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("stockCode", stockCode);
        return stockNetWorthStore.selectMaxNetWorth(params);
    }
}
