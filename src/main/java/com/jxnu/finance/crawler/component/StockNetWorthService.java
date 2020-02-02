package com.jxnu.finance.crawler.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxnu.finance.store.entity.stock.StockExtra;
import com.jxnu.finance.store.entity.stock.StockNetWorth;
import com.jxnu.finance.store.mapper.StockExtraStore;
import com.jxnu.finance.store.mapper.StockNetWorthStore;
import com.jxnu.finance.utils.OkHttpUtils;
import com.jxnu.finance.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<StockNetWorth> stockNetWorths = new ArrayList<StockNetWorth>();
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
}
