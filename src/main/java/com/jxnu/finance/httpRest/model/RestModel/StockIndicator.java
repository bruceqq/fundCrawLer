package com.jxnu.finance.httpRest.model.RestModel;

/**
 * @author yaphyao
 * @version 2018/2/28
 * @see com.jxnu.finance.httpRest.model.RestModel
 */
public class StockIndicator {
    private String totalMarketValue;   //总市值
    private String netWorth;           //净资产
    private String netProfit;          //净利润
    private String grossProfitMargin;  //毛利率
    private String netInterestRate;    //净利率
    private String pe;                 //市盈率
    private String pb;                 //市净率
    private String roe;                //ROE
    private String subject;            //行业
    private String price;              //当前股价




    public String getPe() {
        return pe;
    }

    public void setPe(String pe) {
        this.pe = pe;
    }

    public String getPb() {
        return pb;
    }

    public void setPb(String pb) {
        this.pb = pb;
    }

    public String getTotalMarketValue() {
        return totalMarketValue;
    }

    public void setTotalMarketValue(String totalMarketValue) {
        this.totalMarketValue = totalMarketValue;
    }

    public String getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(String netWorth) {
        this.netWorth = netWorth;
    }

    public String getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(String netProfit) {
        this.netProfit = netProfit;
    }

    public String getGrossProfitMargin() {
        return grossProfitMargin;
    }

    public void setGrossProfitMargin(String grossProfitMargin) {
        this.grossProfitMargin = grossProfitMargin;
    }

    public String getNetInterestRate() {
        return netInterestRate;
    }

    public void setNetInterestRate(String netInterestRate) {
        this.netInterestRate = netInterestRate;
    }

    public String getRoe() {
        return roe;
    }

    public void setRoe(String roe) {
        this.roe = roe;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "StockIndicator{" +
                "totalMarketValue='" + totalMarketValue + '\'' +
                ", netWorth='" + netWorth + '\'' +
                ", netProfit='" + netProfit + '\'' +
                ", grossProfitMargin='" + grossProfitMargin + '\'' +
                ", netInterestRate='" + netInterestRate + '\'' +
                ", pe='" + pe + '\'' +
                ", pb='" + pb + '\'' +
                ", roe='" + roe + '\'' +
                ", subject='" + subject + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
