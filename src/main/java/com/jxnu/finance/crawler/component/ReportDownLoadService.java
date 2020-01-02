package com.jxnu.finance.crawler.component;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxnu.finance.config.CrawlerConfig;
import com.jxnu.finance.utils.OkHttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportDownLoadService {
    @Autowired
    private CrawlerConfig crawlerConfig;

    /**
     * 下载对应股票年报
     *
     * @param stockCode
     */
    public void download(String stockCode) {
        /**
         * 参数
         */
        Map<String, String> params = new HashMap();
        params.put("tabName", "fulltext");
        params.put("stock", stockCode);
        params.put("category", "category_ndbg_szsh");
        /**
         * http 头
         */
        Map<String, String> head = new HashMap<String, String>();
        head.put("Content-Type", "application/x-www-form-urlencoded");
        String result = OkHttpUtils.post(params, head, crawlerConfig.getTiantianStockAnnualReport());
        if (StringUtils.isBlank(result)) {
            return;
        }
        JSONObject jsonObject = JSON.parseObject(result);
        if (jsonObject == null || !jsonObject.containsKey("announcements")) {
            return;
        }
        JSONArray announcements = jsonObject.getJSONArray("announcements");
        if (announcements == null || announcements.isEmpty()) {
            return;
        }
        for (Object announcement : announcements) {
            JSONObject announcementJson = (JSONObject) announcement;
            if (announcementJson == null) {
                continue;
            }
            String stockName = announcementJson.getString("secName");
            String announcementTitle = announcementJson.getString("announcementTitle");
            if (announcementTitle.contains("摘要") || announcementTitle.contains("主要")) {
                continue;
            }
            String adjunctUrl = announcementJson.getString("adjunctUrl");
            String filePath = crawlerConfig.getAnnualReportFilePath() + File.separator + stockName + File.separator + announcementTitle;
            File file = new File(filePath);
            if (file.exists()) {
                continue;
            }
            OkHttpUtils.fileDownLoad(crawlerConfig.getCninfoDownloadUrl() + adjunctUrl, filePath);
        }
    }

    public static void main(String[] args) {

    }
}
