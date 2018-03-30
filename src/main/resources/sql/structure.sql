CREATE DATABASE  IF NOT EXISTS `kidbridge` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `kidbridge`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: dev.51zhiyuan.net    Database: kidbridge
-- ------------------------------------------------------
-- Server version	5.7.18-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_admin`
--

DROP TABLE IF EXISTS `t_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '登录名',
  `password` varchar(128) NOT NULL DEFAULT '' COMMENT '密码',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='管理员信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_article`
--

DROP TABLE IF EXISTS `t_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(32) NOT NULL DEFAULT '' COMMENT '文章标题',
  `content` varchar(64) NOT NULL DEFAULT '' COMMENT '正文内容，暂定存储文本路径',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COMMENT='文章信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_authc`
--

DROP TABLE IF EXISTS `t_authc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_authc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `code` varchar(128) NOT NULL COMMENT '授权码',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '授权类型，默认0微信授权',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_USER_ID_AUTHC_USER_ID_idx` (`user_id`),
  CONSTRAINT `FK_AUTHC_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COMMENT='第三方授权信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_bill`
--

DROP TABLE IF EXISTS `t_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `fee` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '收入、支付费用，正负数表示',
  `option` varchar(512) NOT NULL DEFAULT '' COMMENT '扩展字段，json数据格式，可能包含一些额外信息\nbillType=3/4：user用户编号、type类别(0绘本赏析，1课程赏析，2绘本跟读，3课程跟读)、target目标编号',
  `bill_type` int(11) NOT NULL DEFAULT '0' COMMENT '收支类别，0解锁绘本，1解锁课程，2余额充值，3打赏，4被打赏，5余额兑换，积分扣除，6余额兑换，金额增加，7绘本跟读，8课程跟读，9系统赠送，10系统扣除，11余额充值ios内购支付，12绘本跟读分享，13课程跟读分享',
  `fee_type` int(11) NOT NULL DEFAULT '0' COMMENT '费用类别，0现金，1水滴',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_USER_ID_BILL_USER_ID_idx` (`user_id`),
  CONSTRAINT `FK_BILL_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1682 DEFAULT CHARSET=utf8mb4 COMMENT='费用明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_book`
--

DROP TABLE IF EXISTS `t_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '绘本名称',
  `icon` varchar(1024) NOT NULL DEFAULT '' COMMENT '绘本图标，数组类型',
  `price` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '绘本价格，默认值0免费，无锁',
  `fit` int(11) NOT NULL DEFAULT '0' COMMENT '适合年龄段，枚举类型，0：3-5, 1：6-8, 2：9-12,  3：4-7, 4：8-10',
  `outline` varchar(2048) NOT NULL DEFAULT '' COMMENT '绘本概要',
  `feeling` varchar(2048) NOT NULL DEFAULT '' COMMENT '绘本感悟',
  `difficulty` varchar(32) NOT NULL DEFAULT '' COMMENT '困难度',
  `tag` varchar(128) NOT NULL DEFAULT '' COMMENT '关键词，json数据格式',
  `repeat_active_time` int(11) NOT NULL DEFAULT '0' COMMENT '绘本跟读的有效时间，秒单位',
  `active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否在首页上显示， 默认是',
  `rich_text` varchar(64) NOT NULL DEFAULT '' COMMENT '富文本，七牛云资源名',
  `audio` varchar(64) NOT NULL DEFAULT '',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '绘本排序，数值越大越靠前',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=263 DEFAULT CHARSET=utf8mb4 COMMENT='绘本信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_book_comment`
--

DROP TABLE IF EXISTS `t_book_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_book_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quote_id` int(11) NOT NULL DEFAULT '-1' COMMENT '引用被评论id',
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `book_id` int(11) NOT NULL COMMENT '绘本编号',
  `content` varchar(8192) NOT NULL DEFAULT '' COMMENT '评论内容',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=638 DEFAULT CHARSET=utf8mb4 COMMENT='绘本评论';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_book_copyright`
--

DROP TABLE IF EXISTS `t_book_copyright`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_book_copyright` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `book_id` int(11) NOT NULL COMMENT '绘本编号',
  `user_id` int(11) NOT NULL COMMENT '所属用户编号',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='绘本所属版权';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_book_segment`
--

DROP TABLE IF EXISTS `t_book_segment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_book_segment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `book_id` int(11) NOT NULL COMMENT '所属绘本',
  `icon` varchar(64) NOT NULL DEFAULT '' COMMENT '图标路径，七牛云资源名',
  `text` varchar(2048) NOT NULL DEFAULT '' COMMENT '文本内容',
  `audio` varchar(64) NOT NULL DEFAULT '' COMMENT '音频内容',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_BOOK_SEGMENT_BOOK_ID_idx` (`book_id`),
  CONSTRAINT `FK_BOOK_SEGMENT_BOOK_ID` FOREIGN KEY (`book_id`) REFERENCES `t_book` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7669 DEFAULT CHARSET=utf8mb4 COMMENT='绘本段落信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_bookshelf`
--

DROP TABLE IF EXISTS `t_bookshelf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_bookshelf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '书单',
  `icon` varchar(64) NOT NULL DEFAULT '' COMMENT '书单展示图标',
  `cover` varchar(512) NOT NULL DEFAULT '' COMMENT '书单内封面图片，可能包含链接， json数据格式',
  `fit` int(11) NOT NULL DEFAULT '0' COMMENT '适合年龄段，枚举类型，0：3-5, 1：6-8, 2：9-12',
  `tag` varchar(128) NOT NULL DEFAULT '' COMMENT '关键词，json数据格式',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COMMENT='书单信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_bookshelf_detail`
--

DROP TABLE IF EXISTS `t_bookshelf_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_bookshelf_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bookshelf_id` int(11) NOT NULL COMMENT '书单编号',
  `book_id` int(11) NOT NULL COMMENT '绘本编号',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=181 DEFAULT CHARSET=utf8mb4 COMMENT='书单详情，与绘本关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_bookshelf_hot`
--

DROP TABLE IF EXISTS `t_bookshelf_hot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_bookshelf_hot` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bookshelf_id` int(11) NOT NULL COMMENT '书单编号',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COMMENT='热门书单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_bookshelf_recommend`
--

DROP TABLE IF EXISTS `t_bookshelf_recommend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_bookshelf_recommend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bookshelf_id` int(11) NOT NULL COMMENT '书单编号',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COMMENT='推荐书单， 对应今日打卡';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_config`
--

DROP TABLE IF EXISTS `t_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(512) NOT NULL COMMENT '键',
  `value` varchar(512) NOT NULL COMMENT '值',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='参数配置信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_course`
--

DROP TABLE IF EXISTS `t_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_course` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `teacher_id` int(11) NOT NULL DEFAULT '-1' COMMENT '教师编号',
  `name` varchar(128) NOT NULL DEFAULT '' COMMENT '课程名称',
  `icon` varchar(64) NOT NULL DEFAULT '' COMMENT '课程图标',
  `fit` int(11) NOT NULL DEFAULT '0' COMMENT '适合年龄段，枚举类型，0：3-5, 1：6-8, 2：9-12, 3：4-7, 4：8-10',
  `limit` int(11) NOT NULL DEFAULT '0' COMMENT '课程最多报名人数，0不限',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '课程开始时间',
  `price` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '课程价格',
  `outline` varchar(2048) NOT NULL DEFAULT '' COMMENT '课程概要',
  `tag` varchar(128) NOT NULL DEFAULT '' COMMENT '课程关键词，json数据格式\n[\n  "hello",\n  "world"\n]',
  `rich_text` varchar(64) NOT NULL DEFAULT '' COMMENT '富文本',
  `sort` int(11) DEFAULT '0',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_COURSE_TEACHER_ID_idx` (`teacher_id`),
  CONSTRAINT `FK_COURSE_TEACHER_ID` FOREIGN KEY (`teacher_id`) REFERENCES `t_teacher` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COMMENT='课程信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_course_comment`
--

DROP TABLE IF EXISTS `t_course_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_course_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quote_id` int(11) NOT NULL DEFAULT '-1' COMMENT '引用被评论编号',
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `course_id` int(11) NOT NULL COMMENT '课程编号',
  `content` varchar(8192) NOT NULL DEFAULT '' COMMENT '评论内容',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=143 DEFAULT CHARSET=utf8mb4 COMMENT='课程评论';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_course_copyright`
--

DROP TABLE IF EXISTS `t_course_copyright`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_course_copyright` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) NOT NULL COMMENT '课程编号',
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='课程版权，表示某课程归属的用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_course_detail`
--

DROP TABLE IF EXISTS `t_course_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_course_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` varchar(45) NOT NULL COMMENT '课程编号',
  `book_id` varchar(45) NOT NULL COMMENT '绘本编号',
  `cycle` int(11) NOT NULL DEFAULT '7' COMMENT '课程的绘本跟读周期， 默认7天',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=910 DEFAULT CHARSET=utf8mb4 COMMENT='课程详情，与绘本关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_course_hot`
--

DROP TABLE IF EXISTS `t_course_hot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_course_hot` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) NOT NULL COMMENT '课程编号',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COMMENT='热门课程';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_feedback`
--

DROP TABLE IF EXISTS `t_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `content` varchar(1024) NOT NULL DEFAULT '' COMMENT '反馈内容',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_FEEDBACK_USER_ID_idx` (`user_id`),
  CONSTRAINT `FK_FEEDBACK_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='意见反馈';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_ios_iap_product`
--

DROP TABLE IF EXISTS `t_ios_iap_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_ios_iap_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `price` decimal(8,2) NOT NULL DEFAULT '0.00',
  `product` varchar(128) NOT NULL,
  `des` varchar(32) NOT NULL DEFAULT '',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_UNIQUE` (`product`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_ios_iap_recharge`
--

DROP TABLE IF EXISTS `t_ios_iap_recharge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_ios_iap_recharge` (
  `id` varchar(32) NOT NULL,
  `user_id` int(11) NOT NULL,
  `fee` decimal(8,2) NOT NULL DEFAULT '0.00',
  `ios_iap_product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内购充值';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_medal`
--

DROP TABLE IF EXISTS `t_medal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_medal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '勋章名称',
  `icon` varchar(512) NOT NULL DEFAULT '' COMMENT '勋章图标,json格式，需区点亮及未点亮状态',
  `bonus` int(11) NOT NULL DEFAULT '0' COMMENT '该勋章获得所需水滴数量',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COMMENT='勋章信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_recharge`
--

DROP TABLE IF EXISTS `t_recharge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_recharge` (
  `id` varchar(32) NOT NULL COMMENT '充值单号',
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `fee` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '金额',
  `fee_type` int(11) NOT NULL DEFAULT '0' COMMENT '金额类型，0现金，1积分',
  `method` int(11) NOT NULL DEFAULT '0' COMMENT '支付方式0，本平台，1支付宝，2微信',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态，0未成功，1成功',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='余额充值/兑换表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_search_record`
--

DROP TABLE IF EXISTS `t_search_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_search_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `keyword` varchar(512) NOT NULL DEFAULT '',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=197 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_teacher`
--

DROP TABLE IF EXISTS `t_teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_teacher` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `realname` varchar(16) NOT NULL DEFAULT '' COMMENT '教师姓名',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_TEACHER_USER_ID_idx` (`user_id`),
  CONSTRAINT `FK_TEACHER_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COMMENT='教师信息表，与用户关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(16) NOT NULL DEFAULT '' COMMENT '手机号码',
  `password` varchar(32) NOT NULL DEFAULT '' COMMENT '登录密码',
  `head` varchar(64) NOT NULL DEFAULT '' COMMENT '用户头像',
  `nickname` varchar(50) NOT NULL DEFAULT '' COMMENT '用户昵称',
  `birthday` date DEFAULT NULL COMMENT '用户生日',
  `address` varchar(128) NOT NULL DEFAULT '' COMMENT '用户地址',
  `bonus` int(11) NOT NULL DEFAULT '0' COMMENT '水滴',
  `balance` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `signature` varchar(64) NOT NULL DEFAULT '' COMMENT '签名',
  `receiving_contact` varchar(32) NOT NULL DEFAULT '' COMMENT '收货联系人',
  `receiving_phone` varchar(32) NOT NULL DEFAULT '' COMMENT '收货联系方式',
  `receiving_region` varchar(128) NOT NULL DEFAULT '' COMMENT '收货区域',
  `receiving_street` varchar(128) NOT NULL DEFAULT '' COMMENT '收货街道',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user_book`
--

DROP TABLE IF EXISTS `t_user_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user_book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `book_id` int(11) NOT NULL DEFAULT '0' COMMENT '绘本编号',
  `free` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0:付费 1:免费',
  `option` varchar(512) NOT NULL DEFAULT '' COMMENT '扩展字段，目前只保存课程下的绘本提前解锁预习的赠送信息',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_USER_BOOK_USER_ID_idx` (`user_id`),
  KEY `FK_USER_BOOK_BOOK_ID_idx` (`book_id`),
  CONSTRAINT `FK_USER_BOOK_BOOK_ID` FOREIGN KEY (`book_id`) REFERENCES `t_book` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_USER_BOOK_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=805 DEFAULT CHARSET=utf8mb4 COMMENT='我的绘本';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user_book_repeat`
--

DROP TABLE IF EXISTS `t_user_book_repeat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user_book_repeat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_book_id` int(11) NOT NULL COMMENT '用户编号',
  `segment` longtext NOT NULL COMMENT '跟读详情，json数据格式\n[\n  {\n    "segment": 1,\n    "audio": "/xxxx/xxxx.mp3"\n  },\n  {\n    "book_segment_id": 2,\n    "audio": "/xxxx/xxxx.mp3"\n  }\n]',
  `share` tinyint(1) NOT NULL DEFAULT '0' COMMENT '标记该跟读是否已分享，没有分享加水滴',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_USER_BOOK_REPEAT_USER_BOOK_ID_idx` (`user_book_id`),
  CONSTRAINT `FK_USER_BOOK_REPEAT_USER_BOOK_ID` FOREIGN KEY (`user_book_id`) REFERENCES `t_user_book` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=153 DEFAULT CHARSET=utf8mb4 COMMENT='用户绘本跟读';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user_book_repeat_comment`
--

DROP TABLE IF EXISTS `t_user_book_repeat_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user_book_repeat_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quote_id` int(11) NOT NULL DEFAULT '-1' COMMENT '被评论编号',
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `user_book_repeat_id` int(11) NOT NULL COMMENT '用户跟读编号',
  `content` varchar(8192) NOT NULL DEFAULT '' COMMENT '评论内容，json数据格式\n{\n  "text": "test",\n  "audio": "/xxxx/xxxx.mp3"\n}',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_USER_BOOK_REPEAT_COMMENT_USER_ID_idx` (`user_id`),
  KEY `FK_USER_BOOK_REPEAT_COMMENT_USER_BOOK_REPEAT_ID_idx` (`user_book_repeat_id`),
  CONSTRAINT `FK_USER_BOOK_REPEAT_COMMENT_USER_BOOK_REPEAT_ID` FOREIGN KEY (`user_book_repeat_id`) REFERENCES `t_user_book_repeat` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_USER_BOOK_REPEAT_COMMENT_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=utf8mb4 COMMENT='用户跟读评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user_book_repeat_like`
--

DROP TABLE IF EXISTS `t_user_book_repeat_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user_book_repeat_like` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '点赞的用户编号',
  `user_book_repeat_id` int(11) NOT NULL COMMENT '用户跟读编号',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_USER_BOOK_REPEAT_LIKE_FK_USER_BOOK_REPEAT_COMMENT_USER_B_idx` (`user_book_repeat_id`),
  KEY `FK_USER_BOOK_REPEAT_LIKE_USER_ID_idx` (`user_id`),
  CONSTRAINT `FK_USER_BOOK_REPEAT_LIKE_USER_BOOK_REPEAT_ID` FOREIGN KEY (`user_book_repeat_id`) REFERENCES `t_user_book_repeat` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_USER_BOOK_REPEAT_LIKE_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户跟读点赞信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user_course`
--

DROP TABLE IF EXISTS `t_user_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user_course` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `course_id` int(11) NOT NULL COMMENT '课程编号',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_USER_COURSE_USER_ID_idx` (`user_id`),
  KEY `FK_USER_COURSE_COURSE_ID_idx` (`course_id`),
  CONSTRAINT `FK_USER_COURSE_COURSE_ID` FOREIGN KEY (`course_id`) REFERENCES `t_course` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_USER_COURSE_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=180 DEFAULT CHARSET=utf8mb4 COMMENT='我的课程';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user_course_repeat`
--

DROP TABLE IF EXISTS `t_user_course_repeat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user_course_repeat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_course_id` int(11) NOT NULL COMMENT '课程编号',
  `book_id` int(11) NOT NULL COMMENT '绘本编号',
  `segment` longtext NOT NULL COMMENT '跟读详情，json数据格式\n[\n  {\n    "segment": 1,\n    "audio": "/xxxx/xxxx.mp3"\n  },\n  {\n    "book_segment_id": 2,\n    "audio": "/xxxx/xxxx.mp3"\n  }\n]',
  `share` tinyint(1) NOT NULL DEFAULT '0' COMMENT '标记该跟读是否已分享，没有分享加水滴',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_USER_COURSE_REPEAT_USER_COURSE_ID_idx` (`user_course_id`),
  CONSTRAINT `FK_USER_COURSE_REPEAT_USER_COURSE_ID` FOREIGN KEY (`user_course_id`) REFERENCES `t_user_course` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8mb4 COMMENT='用户课程跟读';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user_course_repeat_comment`
--

DROP TABLE IF EXISTS `t_user_course_repeat_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user_course_repeat_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quote_id` int(11) NOT NULL DEFAULT '-1' COMMENT '被评论编号',
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `user_course_repeat_id` int(11) NOT NULL COMMENT '用户课程跟读编号',
  `content` varchar(8192) NOT NULL DEFAULT '' COMMENT '评论内容',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户课程跟读评论';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user_course_repeat_like`
--

DROP TABLE IF EXISTS `t_user_course_repeat_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user_course_repeat_like` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `user_course_repeat_id` int(11) NOT NULL COMMENT '用户课程跟读编号',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户课程跟读点赞';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_version`
--

DROP TABLE IF EXISTS `t_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device` int(11) NOT NULL DEFAULT '0',
  `content` varchar(1024) NOT NULL DEFAULT '',
  `number` varchar(64) NOT NULL DEFAULT '',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_whirligig`
--

DROP TABLE IF EXISTS `t_whirligig`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_whirligig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `link` varchar(512) NOT NULL COMMENT '文章编号',
  `icon` varchar(64) NOT NULL DEFAULT '' COMMENT '轮播图图标路径',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '轮播图显示位置，0绘本馆 1课程管',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_WHIRLIGIG_ARTICLE_ID_idx` (`link`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COMMENT='首页轮播图信息';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-30 11:25:54
