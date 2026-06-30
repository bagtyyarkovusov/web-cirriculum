-- MySQL dump 10.13  Distrib 9.7.1, for macos26.4 (arm64)
--
-- Host: localhost    Database: bookstore
-- ------------------------------------------------------
-- Server version	9.7.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `audit_log`
--

DROP TABLE IF EXISTS `audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `action` varchar(50) NOT NULL,
  `detail` varchar(1000) DEFAULT NULL,
  `ip` varchar(45) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `hmac` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_audit_created` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_log`
--

LOCK TABLES `audit_log` WRITE;
/*!40000 ALTER TABLE `audit_log` DISABLE KEYS */;
INSERT INTO `audit_log` VALUES (1,1,'customer','LOGIN_SUCCESS','username=customer','0:0:0:0:0:0:0:1','2026-06-30 06:11:38',NULL),(2,1,'customer','CHECKOUT','orderId=20','0:0:0:0:0:0:0:1','2026-06-30 06:11:41',NULL),(3,1,'customer','LOGOUT','path=/app/logout','0:0:0:0:0:0:0:1','2026-06-30 06:11:45',NULL),(4,2,'operator','LOGIN_SUCCESS','username=operator','0:0:0:0:0:0:0:1','2026-06-30 06:11:46',NULL),(5,2,'operator','ADMIN_VIEW','method=GET,path=/admin/orders','0:0:0:0:0:0:0:1','2026-06-30 06:11:51',NULL),(6,2,'operator','ADMIN_VIEW','method=GET,path=/admin/orders','0:0:0:0:0:0:0:1','2026-06-30 06:11:53',NULL),(7,2,'operator','ADMIN_VIEW','method=GET,path=/admin/users','0:0:0:0:0:0:0:1','2026-06-30 06:11:54',NULL),(8,2,'operator','LOGOUT','path=/app/logout','0:0:0:0:0:0:0:1','2026-06-30 06:11:56',NULL),(9,4,'auditor','LOGIN_SUCCESS','username=auditor','0:0:0:0:0:0:0:1','2026-06-30 06:11:58',NULL),(10,4,'auditor','AUDIT_VIEW','method=GET,path=/admin/audit','0:0:0:0:0:0:0:1','2026-06-30 06:11:59',NULL);
/*!40000 ALTER TABLE `audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `author` varchar(100) DEFAULT NULL,
  `publisher` varchar(100) DEFAULT NULL,
  `isbn` varchar(20) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL DEFAULT '0.00',
  `stock` int NOT NULL DEFAULT '0',
  `category_id` bigint DEFAULT NULL,
  `cover_path` varchar(255) DEFAULT NULL,
  `intro` text,
  `status` varchar(10) NOT NULL DEFAULT 'ON',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_book_category` (`category_id`),
  CONSTRAINT `fk_book_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (1,'Java核心技术 卷I（原书第12版）','Cay S. Horstmann','机械工业出版社','9787111709473',149.00,50,1,NULL,'Java入门与进阶的经典教程。','ON','2026-06-30 14:06:16'),(2,'深入理解Java虚拟机（第3版）','周志明','机械工业出版社','9787111641247',129.00,30,1,NULL,'JVM 原理与实战。','ON','2026-06-30 14:06:16'),(3,'算法导论（原书第3版）','Thomas H. Cormen','机械工业出版社','9787111407010',128.00,20,1,NULL,'算法领域的权威著作。','ON','2026-06-30 14:06:16'),(4,'活着','余华','作家出版社','9787506365437',45.00,80,2,NULL,'一部关于生命与苦难的小说。','ON','2026-06-30 14:06:16'),(5,'三体（全三册）','刘慈欣','重庆出版社','9787229137939',168.00,60,2,NULL,'中国科幻里程碑之作。','ON','2026-06-30 14:06:16'),(6,'时间简史','史蒂芬·霍金','湖南科学技术出版社','9787535732309',45.00,40,3,NULL,'探索宇宙与时间的本质。','ON','2026-06-30 14:06:16'),(7,'新概念英语（2）','亚历山大、何其莘','外语教学与研究出版社','9787560022086',38.00,100,4,NULL,'经典英语学习教材。','ON','2026-06-30 14:06:16'),(8,'考研数学复习全书','李永乐','中国农业出版社','9787109267701',89.00,25,5,NULL,'考研数学系统复习用书。','ON','2026-06-30 14:06:16'),(9,'已下架示例图书','示例','示例出版社','0000000000000',1.00,0,1,NULL,'该书已下架，用户端不可见。','OFF','2026-06-30 14:06:16'),(10,'代码大全（第2版）','Steve McConnell','电子工业出版社','9787121026003',128.00,22,1,NULL,'软件构建实践经典。','ON','2026-06-30 14:06:16'),(11,'重构（第2版）','Martin Fowler','人民邮电出版社','9787115508645',99.00,5,1,NULL,'改善既有代码设计。','ON','2026-06-30 14:06:16'),(12,'设计模式','Erich Gamma 等','机械工业出版社','9787111075752',89.00,17,1,NULL,'可复用面向对象软件的基础。','ON','2026-06-30 14:06:16'),(13,'Spring实战（第6版）','Craig Walls','人民邮电出版社','9787115564756',119.00,14,1,NULL,'Spring 框架实战指南。','ON','2026-06-30 14:06:16'),(14,'Clean Code','Robert C. Martin','Prentice Hall','9780132350884',139.00,10,1,NULL,'代码整洁之道。','ON','2026-06-30 14:06:16'),(15,'Effective Java（第3版）','Joshua Bloch','Addison-Wesley','9780134685991',149.00,8,1,NULL,'Java 编程最佳实践。','ON','2026-06-30 14:06:16'),(16,'Python编程：从入门到实践','Eric Matthes','人民邮电出版社','9787115428028',89.00,35,1,NULL,'Python 入门经典。','ON','2026-06-30 14:06:16'),(17,'黑客与画家','Paul Graham','人民邮电出版社','9787115249494',59.00,3,1,NULL,'硅谷创业教父文集。','ON','2026-06-30 14:06:16'),(18,'百年孤独','加西亚·马尔克斯','南海出版公司','9787544253994',55.00,45,2,NULL,'魔幻现实主义文学代表作。','ON','2026-06-30 14:06:16'),(19,'围城','钱钟书','人民文学出版社','9787020090006',39.00,55,2,NULL,'中国现代文学经典。','ON','2026-06-30 14:06:16'),(20,'红楼梦','曹雪芹','人民文学出版社','9787020002207',68.00,40,2,NULL,'中国古典小说巅峰。','ON','2026-06-30 14:06:16'),(21,'平凡的世界','路遥','北京十月文艺出版社','9787530216781',108.00,28,2,NULL,'展现中国城乡社会变迁。','ON','2026-06-30 14:06:16'),(22,'白夜行','东野圭吾','南海出版公司','9787544255899',49.00,33,2,NULL,'悬疑小说代表作。','ON','2026-06-30 14:06:16'),(23,' Norse Mythology','Neil Gaiman','W. W. Norton','9780393356182',75.00,12,2,NULL,'北欧神话新编。','ON','2026-06-30 14:06:16'),(24,'自私的基因','Richard Dawkins','中信出版社','9787508647357',68.00,22,3,NULL,'进化生物学经典。','ON','2026-06-30 14:06:16'),(25,'万物简史','比尔·布莱森','接力出版社','9787544831488',58.00,17,3,NULL,'科学史通俗读物。','ON','2026-06-30 14:06:16'),(26,'量子力学是什么','Sean Carroll','中信出版社','9787521730001',72.00,9,3,NULL,'量子力学入门。','ON','2026-06-30 14:06:16'),(27,'宇宙','卡尔·萨根','广西科学技术出版社','9787555109521',88.00,11,3,NULL,'探索宇宙奥秘。','ON','2026-06-30 14:06:16'),(28,'人类简史','尤瓦尔·赫拉利','中信出版社','9787508647358',68.00,26,3,NULL,'人类发展历程。','ON','2026-06-30 14:06:16'),(29,'牛津高阶英汉双解词典（第10版）','A. S. Hornby','商务印书馆','9787100200572',198.00,15,4,NULL,'权威英语学习词典。','ON','2026-06-30 14:06:16'),(30,'剑桥雅思真题18','Cambridge','剑桥大学出版社','9781009498916',128.00,20,4,NULL,'雅思官方真题集。','ON','2026-06-30 14:06:16'),(31,'Word Power Made Easy','Norman Lewis','Anchor','9781101873854',45.00,18,4,NULL,'词汇学习经典。','ON','2026-06-30 14:06:16'),(32,'考研英语词汇红宝书','俞敏洪','西安交通大学出版社','9787560542386',58.00,7,5,NULL,'考研英语核心词汇。','ON','2026-06-30 14:06:16'),(33,'肖秀荣考研政治1000题','肖秀荣','国家开放大学出版社','9787304098765',62.00,19,5,NULL,'考研政治练习题集。','ON','2026-06-30 14:06:16'),(34,'注册会计师教材：会计','中国注册会计师协会','中国财政经济出版社','9787522301234',89.00,4,5,NULL,'CPA 会计科目教材。','ON','2026-06-30 14:06:16'),(35,'史记','司马迁','中华书局','9787101003048',128.00,16,6,NULL,'中国第一部纪传体通史。','ON','2026-06-30 14:06:16'),(36,'万历十五年','黄仁宇','生活·读书·新知三联书店','9787108004676',42.00,30,6,NULL,'大历史观代表作。','ON','2026-06-30 14:06:16'),(37,'枪炮、病菌与钢铁','贾雷德·戴蒙德','上海译文出版社','9787532744985',68.00,21,6,NULL,'人类社会发展史。','ON','2026-06-30 14:06:16'),(38,'明朝那些事儿','当年明月','中国友谊出版公司','9787505722460',199.00,13,6,NULL,'通俗历史读物。','ON','2026-06-30 14:06:16'),(39,'耶路撒冷三千年','Simon Sebag Montefiore','民主与建设出版社','9787513903291',118.00,6,6,NULL,'耶路撒冷城市史。','ON','2026-06-30 14:06:16'),(40,'罗马帝国衰亡史','爱德华·吉本','商务印书馆','9787100018654',168.00,2,6,NULL,'西方史学巨著。','OFF','2026-06-30 14:06:16'),(41,'经济学原理（第8版）','N. Gregory Mankiw','北京大学出版社','9787301316180',128.00,20,7,NULL,'经济学入门教材。','ON','2026-06-30 14:06:16'),(42,'穷查理宝典','查理·芒格','中信出版社','9787508685168',168.00,11,7,NULL,'芒格智慧箴言录。','ON','2026-06-30 14:06:16'),(43,'从0到1','Peter Thiel','中信出版社','9787508647356',52.00,25,7,NULL,'创业与商业思维。','ON','2026-06-30 14:06:16'),(44,'巴菲特致股东的信','沃伦·巴菲特','机械工业出版社','9787111563006',79.00,14,7,NULL,'投资智慧合集。','ON','2026-06-30 14:06:16'),(45,'财务报表分析','马丁·弗里德森','中国人民大学出版社','9787300234569',98.00,8,7,NULL,'财务分析实务。','ON','2026-06-30 14:06:16'),(46,'创新者的窘境','Clayton Christensen','中信出版社','9787508683737',69.00,5,7,NULL,'商业创新经典。','OFF','2026-06-30 14:06:16'),(47,'哈利·波特与魔法石','J. K. Rowling','人民文学出版社','9787020033430',59.00,40,8,NULL,'魔法世界开启之作。','ON','2026-06-30 14:06:16'),(48,'小王子','圣埃克苏佩里','天津人民出版社','9787201072627',32.00,60,8,NULL,'献给大人的童话。','ON','2026-06-30 14:06:16'),(49,'夏洛的网','E. B. White','上海译文出版社','9787532761339',35.00,28,8,NULL,'关于友谊的童书经典。','ON','2026-06-30 14:06:16'),(50,'窗边的小豆豆','黑柳彻子','南海出版公司','9787544241984',45.00,15,8,NULL,'日本儿童文学经典。','ON','2026-06-30 14:06:16');
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_item`
--

DROP TABLE IF EXISTS `cart_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `book_id` bigint NOT NULL,
  `qty` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cart_user_book` (`user_id`,`book_id`),
  KEY `fk_cart_book` (`book_id`),
  CONSTRAINT `fk_cart_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_item`
--

LOCK TABLES `cart_item` WRITE;
/*!40000 ALTER TABLE `cart_item` DISABLE KEYS */;
INSERT INTO `cart_item` VALUES (1,5,11,1),(2,5,3,2),(3,6,4,1),(4,7,7,3);
/*!40000 ALTER TABLE `cart_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_category_parent` (`parent_id`),
  CONSTRAINT `fk_category_parent` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'计算机',NULL),(2,'文学艺术',NULL),(3,'科技',NULL),(4,'外语',NULL),(5,'考试辅导',NULL),(6,'历史',NULL),(7,'经济管理',NULL),(8,'童书',NULL);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `book_id` bigint NOT NULL,
  `title_snapshot` varchar(200) DEFAULT NULL,
  `price_snapshot` decimal(10,2) NOT NULL DEFAULT '0.00',
  `qty` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_oi_order` (`order_id`),
  KEY `fk_oi_book` (`book_id`),
  CONSTRAINT `fk_oi_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `fk_oi_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (1,1,1,'Java核心技术 卷I（原书第12版）',149.00,1),(2,2,10,'代码大全（第2版）',128.00,1),(3,3,5,'三体（全三册）',168.00,1),(4,3,48,'小王子',32.00,1),(5,4,5,'三体（全三册）',168.00,1),(6,5,8,'考研数学复习全书',89.00,1),(7,6,6,'时间简史',45.00,1),(8,7,2,'深入理解Java虚拟机（第3版）',129.00,1),(9,7,4,'活着',45.00,1),(10,7,6,'时间简史',45.00,1),(11,8,7,'新概念英语（2）',38.00,1),(12,8,19,'围城',39.00,1),(13,9,1,'Java核心技术 卷I（原书第12版）',149.00,1),(14,10,4,'活着',45.00,1),(15,10,48,'小王子',32.00,1),(16,11,10,'代码大全（第2版）',128.00,1),(17,12,11,'重构（第2版）',99.00,1),(18,13,8,'考研数学复习全书',89.00,1),(19,14,5,'三体（全三册）',168.00,1),(20,15,6,'时间简史',45.00,1),(21,16,10,'代码大全（第2版）',128.00,1),(22,17,29,'牛津高阶英汉双解词典（第10版）',198.00,1),(23,18,24,'自私的基因',68.00,1),(24,19,11,'重构（第2版）',99.00,1),(25,20,12,'设计模式',89.00,1);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(32) NOT NULL,
  `user_id` bigint NOT NULL,
  `total` decimal(10,2) NOT NULL DEFAULT '0.00',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING_PAY',
  `receiver_snapshot` varchar(512) DEFAULT NULL,
  `tracking_no` varchar(50) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `paid_at` datetime DEFAULT NULL,
  `shipped_at` datetime DEFAULT NULL,
  `completed_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_orders_no` (`order_no`),
  KEY `fk_orders_user` (`user_id`),
  CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'O2026063008010000001001',1,149.00,'COMPLETED','张三 / 北京','YT202606300001','2026-06-20 08:01:00','2026-06-20 08:05:00','2026-06-21 09:00:00','2026-06-25 16:00:00'),(2,'O2026061509120000002001',5,128.00,'PENDING_PAY','王芳 / 北京',NULL,'2026-06-15 09:12:00',NULL,NULL,NULL),(3,'O2026061814030000003001',6,200.00,'PENDING_SHIP','李华 / 上海',NULL,'2026-06-18 14:03:00','2026-06-18 14:05:00',NULL,NULL),(4,'O2026062010450000004001',7,168.00,'SHIPPED','赵磊 / 广州','YT202606200001','2026-06-20 10:45:00','2026-06-20 10:46:00','2026-06-21 09:00:00',NULL),(5,'O2026062511300000005001',8,89.00,'COMPLETED','孙丽 / 成都','YT202606250002','2026-06-25 11:30:00','2026-06-25 11:31:00','2026-06-26 08:30:00','2026-06-28 16:00:00'),(6,'O2026062812150000006001',9,45.00,'CANCELLED','周宇 / 武汉',NULL,'2026-06-28 12:15:00',NULL,NULL,NULL),(7,'O2026061018300000007001',1,219.00,'COMPLETED','张三 / 北京','YT202606100003','2026-06-10 18:30:00','2026-06-10 18:32:00','2026-06-11 10:00:00','2026-06-14 14:00:00'),(8,'O2026062211000000008001',5,77.00,'PENDING_SHIP','王芳 / 北京',NULL,'2026-06-22 11:00:00','2026-06-22 11:02:00',NULL,NULL),(9,'O2026062316200000009001',6,149.00,'SHIPPED','李华 / 上海','YT202606230004','2026-06-23 16:20:00','2026-06-23 16:21:00','2026-06-24 09:30:00',NULL),(10,'O2026062610000000010001',7,77.00,'COMPLETED','赵磊 / 广州','YT202606260005','2026-06-26 10:00:00','2026-06-26 10:01:00','2026-06-27 08:00:00','2026-06-29 10:00:00'),(11,'O2026062909150000011001',8,128.00,'PENDING_PAY','孙丽 / 成都',NULL,'2026-06-29 09:15:00',NULL,NULL,NULL),(12,'O2026061414000000012001',9,99.00,'CANCELLED','周宇 / 武汉',NULL,'2026-06-14 14:00:00',NULL,NULL,NULL),(13,'O2026061709300000013001',1,89.00,'PENDING_SHIP','张三 / 北京',NULL,'2026-06-17 09:30:00','2026-06-17 09:31:00',NULL,NULL),(14,'O2026061908000000014001',5,168.00,'SHIPPED','王芳 / 北京','YT202606190006','2026-06-19 08:00:00','2026-06-19 08:01:00','2026-06-20 09:00:00',NULL),(15,'O2026062409300000015001',6,45.00,'COMPLETED','李华 / 上海','YT202606240007','2026-06-24 09:30:00','2026-06-24 09:31:00','2026-06-25 08:30:00','2026-06-27 12:00:00'),(16,'O2026062107000000016001',7,128.00,'PENDING_PAY','赵磊 / 广州',NULL,'2026-06-21 07:00:00',NULL,NULL,NULL),(17,'O2026061309450000017001',8,198.00,'CANCELLED','孙丽 / 成都',NULL,'2026-06-13 09:45:00',NULL,NULL,NULL),(18,'O2026061611300000018001',9,68.00,'SHIPPED','周宇 / 武汉','YT202606160008','2026-06-16 11:30:00','2026-06-16 11:31:00','2026-06-17 09:00:00',NULL),(19,'O2026062718000000019001',1,99.00,'PENDING_PAY','张三 / 北京',NULL,'2026-06-27 18:00:00',NULL,NULL,NULL),(20,'O2026063014114000018812',1,89.00,'PENDING_SHIP',NULL,NULL,'2026-06-30 06:11:41','2026-06-30 06:11:41',NULL,NULL);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password_sm3` char(64) NOT NULL,
  `salt` varchar(32) NOT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `phone_enc` varchar(255) DEFAULT NULL,
  `address_enc` varchar(512) DEFAULT NULL,
  `role` varchar(20) NOT NULL DEFAULT 'CUSTOMER',
  `status` varchar(10) NOT NULL DEFAULT 'ACTIVE',
  `fail_count` int NOT NULL DEFAULT '0',
  `lock_until` datetime DEFAULT NULL,
  `pwd_changed_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'customer','da4886d49f5694d5d252baf1d90d8bc02030469eba613ff499d0eb1b6e13ad93','74ae25abc1b9080dcc90715881962b30','张三','M','CMrkDR2GIUqkqPDuFHU02RkvAMXLthXv3AA0gPzcP2c=',NULL,'CUSTOMER','ACTIVE',0,NULL,'2026-06-30 14:06:16','2026-06-30 14:06:16'),(2,'operator','71128fa23502a7d52ecad197b7bfad87312b292632f9870bd111cae62d49afcb','3c8d7cfeface30b4bd4d4b99a2975f4d','运营小李','F','VsxRrmLzs69DA+f70F2/8MpaIhgPZsNItagkx+858g0=',NULL,'OPERATOR_ADMIN','ACTIVE',0,NULL,'2026-06-30 14:06:16','2026-06-30 14:06:16'),(3,'sysadmin','17d736a9a3c8088994a46b40bab10d376e4e5a0420e36c0af1e0f7beb974f224','9e495e1d9d34d27642d0f1db8e9862e7','系统管理员','M','gt42I0mTS2nJaJa1x8syntYwLBguetCbMwVQ82kmjnA=',NULL,'SYSTEM_ADMIN','ACTIVE',0,NULL,'2026-06-30 14:06:16','2026-06-30 14:06:16'),(4,'auditor','57bea300cff3f3f5c048c8ea1d3c72c66c9ad114cffadf985488d8a72e2f2712','f0bea610b392a9be65b6657e87f88557','审计员','F','42BHvTuMi4xNcKZgNrVruLtMV2oOWmSzcRuk6s3wqi8=',NULL,'AUDIT_ADMIN','ACTIVE',0,NULL,'2026-06-30 14:06:16','2026-06-30 14:06:16'),(5,'wangfang','da4886d49f5694d5d252baf1d90d8bc02030469eba613ff499d0eb1b6e13ad93','74ae25abc1b9080dcc90715881962b30','王芳','F',NULL,NULL,'CUSTOMER','ACTIVE',0,NULL,'2026-06-30 14:06:16','2026-06-30 14:06:16'),(6,'lihua','da4886d49f5694d5d252baf1d90d8bc02030469eba613ff499d0eb1b6e13ad93','74ae25abc1b9080dcc90715881962b30','李华','M',NULL,NULL,'CUSTOMER','ACTIVE',0,NULL,'2026-06-30 14:06:16','2026-06-30 14:06:16'),(7,'zhaolei','da4886d49f5694d5d252baf1d90d8bc02030469eba613ff499d0eb1b6e13ad93','74ae25abc1b9080dcc90715881962b30','赵磊','M',NULL,NULL,'CUSTOMER','ACTIVE',0,NULL,'2026-06-30 14:06:16','2026-06-30 14:06:16'),(8,'sunli','da4886d49f5694d5d252baf1d90d8bc02030469eba613ff499d0eb1b6e13ad93','74ae25abc1b9080dcc90715881962b30','孙丽','F',NULL,NULL,'CUSTOMER','ACTIVE',0,NULL,'2026-06-30 14:06:16','2026-06-30 14:06:16'),(9,'zhouyu','da4886d49f5694d5d252baf1d90d8bc02030469eba613ff499d0eb1b6e13ad93','74ae25abc1b9080dcc90715881962b30','周宇','M',NULL,NULL,'CUSTOMER','ACTIVE',0,NULL,'2026-06-30 14:06:16','2026-06-30 14:06:16');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-30 14:12:27
