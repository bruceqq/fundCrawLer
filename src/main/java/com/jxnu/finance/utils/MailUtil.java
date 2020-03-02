package com.jxnu.finance.utils;


import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author shoumiao_yao
 * @date 2016-10-09ØØØ
 */
public class MailUtil {
    private final static Logger logger= LoggerFactory.getLogger(MailUtil.class);
    private final static RateLimiter rateLimiter = RateLimiter.create(0.05);
    public static void sendmail(String title,String text)  {
        try {
            //发送简单邮件
            SimpleEmail email = new SimpleEmail();

            email.setHostName("smtp.qq.com");
            email.setDebug(true);
            email.setStartTLSEnabled(true);
            email.setSSLOnConnect(true);

            //需要邮件发送服务器验证,用户名/密码
            email.setAuthentication("1247793952@qq.com", "ddnrrbkdolyugjac");
            email.setFrom("1247793952@qq.com");
            email.addTo(new String[]{"15889326057@163.com","1092921095@qq.com"});

            //设置主题的字符集为UTF-8
            email.setSubject(title);
            email.buildMimeMessage();
            email.getMimeMessage().setContent(text,"text/html;charset=utf-8");
            if (rateLimiter.tryAcquire(1, TimeUnit.HOURS)) {
                rateLimiter.acquire();
                email.sendMimeMessage();
            }
        } catch (Exception e) {
            logger.error("mail error:{}", ExceptionUtils.getStackTrace(e));
        }
    }
}
