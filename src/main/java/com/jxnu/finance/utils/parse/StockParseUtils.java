package com.jxnu.finance.utils.parse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxnu.finance.config.enmu.UrlEnmu;
import com.jxnu.finance.httpRest.model.RestModel.StockIndicator;
import com.jxnu.finance.store.entity.fund.FundStock;
import com.jxnu.finance.store.entity.stock.StockiftBean;
import com.jxnu.finance.utils.CacheUtils;
import com.jxnu.finance.utils.CalculateUtil;
import com.jxnu.finance.utils.NumberUtil;
import com.jxnu.finance.utils.OkHttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 股票解析工具类
 */
public class StockParseUtils {
    private static final Logger logger = LoggerFactory.getLogger(StockParseUtils.class);

    /**
     * 基金重仓股票
     *
     * @param url
     * @param fundCode
     * @return
     */
    public static List<FundStock> parseStock(String url, String fundCode, String time, String stockUrl) {
        List<FundStock> stocks = new ArrayList<FundStock>();
        Document document = OkHttpUtils.parseToDocument(url, "utf-8");
        Elements elements = document.getElementsByTag("tbody");
        if (elements.isEmpty()) return stocks;
        for (Element element : elements) {
            Elements trElements = element.getElementsByTag("tr");
            for (Element trElement : trElements) {
                Elements tdElements = trElement.getElementsByTag("td");
                if (tdElements.isEmpty() || tdElements.size() < 3) continue;
                FundStock stock = new FundStock();
                Element stockCodeElement = tdElements.get(1);
                if (stockCodeElement == null) {
                    continue;
                }
                /**
                 * 股票代码
                 */
                String stockCode = stockCodeElement.text();
                if (CacheUtils.get(stockCode, null) != null) {
                    continue;
                }
                /**
                 * 市盈率
                 */
                String newStockUrl = stockUrl;
                String oldStockUrl = "http://push2.eastmoney.com/api/qt/slist/get?spt=1&np=3&fltt=2&invt=2&fields=f9,f12,f13,f14,f20,f23,f37,f45,f49,f134,f135,f129,f1000,f2000,f3000&ut=bd1d9ddb04089700cf9c27f6f7426281&secid=";
                if (stockCode.startsWith("00") || stockCode.startsWith("3")) {
                    newStockUrl = stockUrl.replace("#", "sz" + stockCode);
                    oldStockUrl += "0." + stockCode;
                } else {
                    newStockUrl = stockUrl.replace("#", "sh" + stockCode);
                    oldStockUrl += "1." + stockCode;
                }
                StockIndicator stockIndicator = parseEastMoney(oldStockUrl);
                if (stockIndicator != null) BeanUtils.copyProperties(stockIndicator, stock);
                /**
                 * 分红
                 */
                String shareOutUrl = "http://dcfm.eastmoney.com/EM_MutiSvcExpandInterface/api/js/get?type=DCSOBS&token=70f12f2f4f091e459a279469fe49eca5&p=1&ps=50&sr=-1&st=ReportingPeriod&filter=&cmd=#&js=var%20CnPwAIGw={pages:(tp),data:(x)}&rt=52523179";
                shareOutUrl = shareOutUrl.replace("#", stockCode);
                Float shareOut = shareOut(shareOutUrl);
                /**
                 * 股票名称
                 */
                Element stockNameElement = tdElements.get(2);
                stock.setStockCode(stockCode);
                stock.setStockName(stockNameElement.text());
                stock.setFundCode(fundCode);
                stock.setTime(time);
                stock.setPrice(StockParseUtils.stockPrice(stockCode));
                stock.setStockUrl(newStockUrl);
                stock.setTotalShare(shares(stockCode));
                if (shareOut != null) {
                    stock.setShareOut(shareOut.toString());
                }
                stocks.add(stock);
                CacheUtils.put(stockCode, stockCode);
            }
        }
        return stocks;
    }

    /**
     * 获取股票总股本
     *
     * @param stockCode
     * @return
     */
    private static String shares(String stockCode) {
        try {
            Map<String, String> json = new HashMap();
            json.put("url", "PCF10/RptLatestTarget2");
            JSONObject jsonObject = new JSONObject();
            if (stockCode.startsWith("00") || stockCode.startsWith("3")) {
                stockCode += ".SZ";
            } else {
                stockCode += ".SH";
            }
            jsonObject.put("SecurityCode", stockCode);
            json.put("postData", jsonObject.toJSONString());
            json.put("type", "post");
            json.put("remove", "DRROE,DRPRPAA,incomeIncreaseBy,profitsIncreaseBy,DeductedEps,DilutedEps,ReportDate,Reason");
            String body = OkHttpUtils.post(json, null, UrlEnmu.stock_share.url());
            JSONArray jsonArray = JSONArray.parseArray(body);
            if (CollectionUtils.isEmpty(jsonArray)) return "";
            JSONObject shareJson = (JSONObject) jsonArray.get(0);
            return shareJson.getBigDecimal("TOTALSHARE").divide(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 股票解禁记录
     *
     * @param fundCode
     * @return
     */
    public static List<StockiftBean> parseLiftBean(String fundCode) {
        List<StockiftBean> liftBeans = new ArrayList<StockiftBean>();
        String url = UrlEnmu.stock_lift_bean.url().replace("@", fundCode).replace("#", String.valueOf(new Date().getTime()));
        String document = OkHttpUtils.parseToString(url);
        JSONArray jsonArray = JSONArray.parseArray(document);
        jsonArray.size();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Object object : jsonArray) {
            StockiftBean liftBean = new StockiftBean();
            JSONObject liftBeanInfo = (JSONObject) object;
            liftBean.setLiftBeanTime(dateFormat.format(liftBeanInfo.getDate("ltsj")));
            liftBean.setLiftBeanShare(liftBeanInfo.getString("xsglx"));
            liftBeans.add(liftBean);
        }
        return liftBeans;
    }

    /**
     * 股价
     *
     * @param stockCode
     * @return
     */
    private static String stockPrice(String stockCode) {
        String c = "";
        String url = UrlEnmu.stock_price.url();
        url = url.replace("#", String.valueOf(new Date().getTime()));
        if (stockCode.startsWith("00") || stockCode.startsWith("3")) {
            url = url.replace("@", stockCode + "2");
        } else {
            url = url.replace("@", stockCode + "1");
        }
        Document document = OkHttpUtils.parseToDocument(url, "utf-8");
        String text = document.text();
        if (StringUtils.isNotBlank(text)) {
            text = text.substring(1, text.length() - 1);
        }
        JSONObject jsonObject = JSONObject.parseObject(text);
        if (jsonObject != null) {
            JSONObject infoJson = jsonObject.getJSONObject("info");
            if (infoJson != null) {
                c = infoJson.getString("c");
            }
        }
        return c;
    }


    /**
     * 股票市盈率
     *
     * @param url
     * @return
     */
    private static StockIndicator parseEastMoney(String url) {
        Double totalMarketValue;
        Double netWorth;
        Double netProfit;
        Double grossProfitMargin;
        Double netInterestRate;
        Double pe;
        Double pb;
        Double roe;
        String subject = "";
        StockIndicator stockIndicator = new StockIndicator();
        String response = OkHttpUtils.parseToString(url);
        if (StringUtils.isBlank(response)) {
            return stockIndicator;
        }
        logger.info("url:{},response:{}", url, response);
        JSONObject json = (JSONObject) JSONObject.parse(response);
        if (json == null) {
            return stockIndicator;
        }
        JSONObject dataJson = json.getJSONObject("data");
        if (dataJson == null) {
            return stockIndicator;
        }
        JSONArray jsonArray = dataJson.getJSONArray("diff");
        if (jsonArray == null || jsonArray.isEmpty()) {
            return stockIndicator;
        }
        JSONObject stockInfo = (JSONObject) jsonArray.get(0);
        JSONObject industryInfo = (JSONObject) jsonArray.get(1);
        if (stockInfo == null || industryInfo == null) {
            return stockIndicator;
        }

        totalMarketValue = NumberUtil.calculate(stockInfo.getBigDecimal("f20"));

        pb = stockInfo.getDoubleValue("f23");
        pe = stockInfo.getDoubleValue("f9");
        netWorth = NumberUtil.calculate(stockInfo.getBigDecimal("f135"));
        netProfit = NumberUtil.calculate(stockInfo.getBigDecimal("f45"));
        subject = industryInfo.getString("f14");
        if (StringUtils.isNotBlank(subject) &&
            subject.contains("(行业平均)")) {
            subject = subject.replace("(行业平均)", "");
        }
        grossProfitMargin = stockInfo.getDoubleValue("f49");
        netInterestRate = stockInfo.getDoubleValue("f129");
        roe = stockInfo.getDouble("f37");

        stockIndicator.setTotalMarketValue(totalMarketValue.toString());
        stockIndicator.setNetWorth(netWorth.toString());
        stockIndicator.setNetProfit(netProfit.toString());
        stockIndicator.setPe(pe.toString());
        stockIndicator.setPb(pb.toString());
        stockIndicator.setGrossProfitMargin(grossProfitMargin.toString());
        stockIndicator.setNetInterestRate(netInterestRate.toString());
        stockIndicator.setRoe(roe.toString());
        stockIndicator.setSubject(subject);
        return stockIndicator;
    }

    public static Float shareOut(String url) {
        if (StringUtils.isBlank(url)) {
            return 0.0f;
        }
        String document = OkHttpUtils.parseToString(url);
        if (StringUtils.isNotBlank(document)) {
            try {
                document = document.substring(document.indexOf("=") + 1);
                JSONObject jsonObject = (JSONObject) JSONObject.parse(document);
                if (jsonObject != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONObject shareOutJson = (JSONObject) jsonArray.get(0);
                    if (shareOutJson != null) {
                        document = shareOutJson.getString("AllocationPlan");
                    }
                }
                if (StringUtils.isNotBlank(document)
                    && document.contains("派")) {
                    Double shareOut = Double.parseDouble(document.substring(document.indexOf("派") + 1, document.indexOf("元")).trim());
                    if (shareOut != null) {
                        Float result = CalculateUtil.divide(shareOut.floatValue(), 10.0f, 4);
                        return result;
                    }
                }
            } catch (Exception e) {
            }
        }
        return 0.0f;
    }


    public static void main(String[] args) {

    }

}
