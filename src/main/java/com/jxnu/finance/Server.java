package com.jxnu.finance;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;

public class
Server {
    private final static Logger logger = LoggerFactory.getLogger(Server.class);
    public final static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring.xml");
             countDownLatch.await();
        } catch (Exception e) {
            logger.error("error:{}", ExceptionUtils.getMessage(e));
        }
    }
}
