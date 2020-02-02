# 基金爬虫说明文档
### 使用方式
#### config.properties
1. 配置数据库地址 账号及密码
2. company.switch配置项 （-1:不爬基金公司。0: 爬）
3. fund.switch配置项 （-1:不爬基金信息 0: 爬）
4. fundNetWorth.switch配置项 （-1:不爬基金净值信息 0: 爬基金所有的净值  数字:爬最近几天数据）
#### 启动
com.jxnu.finance.Server
### 爬虫线程
- com.jxnu.finance.crawler.grabThread.specific.CompanyGrab 基金公司爬虫线程
- com.jxnu.finance.crawler.grabThread.specific.FundGrab 基金爬虫线程
- com.jxnu.finance.crawler.grabThread.specific.FundIndexGrab 大盘指数爬虫线程
- com.jxnu.finance.crawler.grabThread.specific.FundNetWorthGrab 基金净值爬虫线程
### 策略
1. 每天净值爬虫   执行相应的策略链表 com.jxnu.finance.crawler.strategy.singleFundNetWorth.BaseSingleNetWorthStrategy
2. 净值爬虫执行前和后 执行相应的策略链表 com.jxnu.finance.crawler.strategy.multiFundNetWorth.AfterHandlerNetWorthStrategy
### 数据库表 fund_crawler
* 基金公司表
 ```
 CREATE TABLE tbl_company_info (
  id int(11) unsigned NOT NULL AUTO_INCREMENT,
  code int(20) DEFAULT NULL,
  name varchar(200) DEFAULT NULL,
  create_time varchar(20) DEFAULT NULL,
  fund_num int(10) DEFAULT NULL,
  handler varchar(200) DEFAULT NULL,
  scale double(10,2) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT       
  CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY code (code) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8mb4;
```

* 基金信息表
```
CREATE TABLE tbl_fund_info (
  id int(11) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(200) DEFAULT NULL,
  code varchar(20) DEFAULT NULL,
  handler varchar(200) DEFAULT NULL,
  type varchar(20) DEFAULT NULL,
  company_code varchar(20) DEFAULT NULL,
  company_name varchar(200) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY code (code) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=4823 DEFAULT CHARSET=utf8mb4;
```

* 基金净值表
```
CREATE TABLE tbl_fund_net_worth (
  id int(11) NOT NULL AUTO_INCREMENT,
  fund_code int(11) DEFAULT NULL,
  time varchar(20) DEFAULT NULL,
  net_worth float DEFAULT NULL,
  rate float DEFAULT NULL,
  PRIMARY KEY (id)
  UNIQUE KEY fund_code_time (fund_code,time),
  KEY time (time),
  KEY func_code (fund_code)
) ENGINE=InnoDB AUTO_INCREMENT=1014703 DEFAULT CHARSET=utf8mb4;
```
* 上证 深证指数表
```
CREATE TABLE tbl_fund_index  (
   id  int(11) NOT NULL AUTO_INCREMENT,
   code  int(11) DEFAULT NULL,
   name  varchar(200) DEFAULT NULL,
   latest_price  float(11,4) DEFAULT NULL,
   change_amout  float(11,4) DEFAULT NULL,
   turnover  float(30,4) DEFAULT NULL,
   volume  float(30,4) DEFAULT NULL,
   yesterday  float(11,4) DEFAULT NULL,
   today  float(11,4) DEFAULT NULL,
   max  float(11,4) DEFAULT NULL,
   min  float(11,4) DEFAULT NULL,
   time  varchar(10) DEFAULT NULL,
   ratio  float(4,2) DEFAULT NULL,
  PRIMARY KEY ( id ),
  UNIQUE KEY  uindex  ( code , latest_price , change_amout , turnover , time , ratio )
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
```
* 基金排名表
 ```
 CREATE TABLE  tbl_fund_rank  (
   id  int(11) NOT NULL AUTO_INCREMENT,
   fund_code  int(11) NOT NULL,
   net_worth  float DEFAULT NULL,
   ratio  float DEFAULT NULL,
   time  varchar(10) NOT NULL,
  PRIMARY KEY ( id ),
  UNIQUE KEY  fund_code_name  ( fund_code , time )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
* 基金分红表
```
CREATE TABLE  tbl_fund_share_out  (
   id  int(11) unsigned NOT NULL AUTO_INCREMENT,
   fund_code  varchar(11) DEFAULT NULL COMMENT '基金代码',
   time  varchar(11) DEFAULT NULL COMMENT '分红时间',
   create_time  timestamp NULL DEFAULT NULL COMMENT '创建时间',
   update_time  timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY ( id ),
  UNIQUE KEY  fund_code_time  ( time , fund_code )
) ENGINE=InnoDB AUTO_INCREMENT=1744 DEFAULT CHARSET=utf8mb4;
```
* 关注基金表
```
CREATE TABLE  tbl_attention_fund  (
   id  int(11) NOT NULL AUTO_INCREMENT,
   fund_name  varchar(200) DEFAULT NULL,
   fund_code  int(11) DEFAULT NULL,
   subject  varchar(256) DEFAULT NULL,
  PRIMARY KEY ( id ),
  UNIQUE KEY  fund_code  ( fund_code )
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;
```
* 基金分析表
```
CREATE TABLE  tbl_day_fund_analyze  (
   id  int(11) unsigned NOT NULL AUTO_INCREMENT,
   code  varchar(20) DEFAULT NULL,
   time  varchar(20) DEFAULT NULL,
   type  varchar(1) DEFAULT '1',
  PRIMARY KEY ( id ),
  UNIQUE KEY  idx_tbl_mail_code_time_type  ( code , time , type )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
* 基金标准差表
```
CREATE TABLE  tbl_fund_standard_deviation  (
   id  int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
   fund_code  varchar(11) DEFAULT NULL COMMENT '基金代码',
   standard_deviation  float DEFAULT NULL COMMENT '标准差',
   average  float DEFAULT NULL COMMENT '平均净值',
   max  float DEFAULT NULL COMMENT '最大值',
   max_aver_rate  float DEFAULT NULL COMMENT '最大值比例',
   min  float DEFAULT NULL COMMENT '最小值',
   min_aver_rate  float DEFAULT NULL COMMENT '最小值比例',
   state  int(1) DEFAULT NULL COMMENT '当前净值状态',
   min_rate  float DEFAULT NULL COMMENT '最小值比例',
   max_rate  float DEFAULT NULL COMMENT '最大值比例',
  PRIMARY KEY ( id ),
  UNIQUE KEY  fundCOde  ( fund_code )
) ENGINE=InnoDB AUTO_INCREMENT=651 DEFAULT CHARSET=utf8mb4;
```
* 邮箱表
```
CREATE TABLE  tbl_mail  (
   id  int(11) unsigned NOT NULL AUTO_INCREMENT,
   code  varchar(20) DEFAULT NULL,
   time  varchar(20) DEFAULT NULL,
   type  varchar(1) DEFAULT '1',
  PRIMARY KEY ( id ),
  UNIQUE KEY  idx_tbl_mail_code_time_type  ( code , time , type )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
* 基金定投表
```
CREATE TABLE  tbl_strategy_crontab  (
   id  int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   crontab_name  varchar(256) DEFAULT NULL COMMENT '定投名称',
   fund_code  int(11) DEFAULT NULL COMMENT '基金',
   fund_name  varchar(50) DEFAULT NULL COMMENT '基金名称',
   start_time  varchar(10) DEFAULT NULL COMMENT '定投开始时间',
   end_time  varchar(10) DEFAULT NULL COMMENT '定投结束时间',
   amount  float DEFAULT NULL COMMENT '定投金额',
   buy_rate  float DEFAULT NULL COMMENT '买入费率',
   sell_rate  float DEFAULT NULL COMMENT '卖出费率',
   create_time  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
   update_time  timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
   state  int(1) DEFAULT NULL COMMENT '定投状态',
  PRIMARY KEY ( id )
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
```
* 定投分析表
```
CREATE TABLE  tbl_strategy_crontab_analyze  (
   crontab_id  int(11) NOT NULL AUTO_INCREMENT COMMENT '定投任务id',
   fund_code  int(11) DEFAULT NULL COMMENT '基金代码',
   aver_net_worth  float DEFAULT NULL COMMENT '平均净值',
   fund_name  varchar(225) DEFAULT NULL COMMENT '基金名称',
   sell_net_worth  float DEFAULT NULL COMMENT '基金卖出净值',
   crontab_amount  float DEFAULT NULL COMMENT '定投金额',
   crontab_share  float DEFAULT NULL COMMENT '定投份额',
   crontab_num  int(11) DEFAULT NULL COMMENT '定投期数',
   rate  float DEFAULT NULL COMMENT '当前收益',
   net_worth  float DEFAULT NULL COMMENT '最新净值',
  PRIMARY KEY ( crontab_id ),
  UNIQUE KEY  unx_crontab  ( crontab_id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
* 定投卖出表
```
CREATE TABLE  tbl_strategy_crontab_sell  (
   id  int(11) unsigned NOT NULL AUTO_INCREMENT,
   crontab_id  int(11) unsigned NOT NULL COMMENT '定时任务id',
   time  varchar(11) NOT NULL DEFAULT '' COMMENT '卖出时间',
   share  float NOT NULL COMMENT '卖出份额',
   net_worth  float NOT NULL COMMENT '卖出净值',
   amount  float NOT NULL COMMENT '卖出金额',
   rate  float NOT NULL COMMENT '收益比例',
   end_time  varchar(10) NOT NULL DEFAULT '' COMMENT '卖出截止时间',
  PRIMARY KEY ( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
* 定投购买记录表
```
CREATE TABLE  tbl_strategy_purchase  (
   id  int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   crontab_id  int(11) DEFAULT NULL COMMENT '策略id',
   fund_code  int(11) DEFAULT NULL COMMENT '基金代码',
   fund_name  varchar(225) DEFAULT NULL COMMENT '基金名称',
   time  varchar(10) DEFAULT NULL COMMENT '购买时间',
   net_worth  float DEFAULT NULL COMMENT '购买净值',
   share  float DEFAULT NULL COMMENT '购买份额',
   amount  float DEFAULT NULL COMMENT '购买金额',
   create_time  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
   update_time  timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
   state  int(1) NOT NULL DEFAULT '0' COMMENT '状态',
  PRIMARY KEY ( id ),
  KEY  idx_crontab  ( crontab_id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
###捐赠

