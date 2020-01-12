package com.jxnu.finance.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class CrawlerConfig {
    @Value("${annualReport.file.path}")
    private String annualReportFilePath;
    @Value("${cninfo.downloadUrl}")
    private String cninfoDownloadUrl;
    @Value("${tiantian.stockAnnualReport}")
    private String tiantianStockAnnualReport;
    @Value("${tiantian.stockholder}")
    private String tiantianStockholder;
}
