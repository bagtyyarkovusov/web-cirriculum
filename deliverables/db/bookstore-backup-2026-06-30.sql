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
-- Current Database: `bookstore`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `bookstore` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `bookstore`;

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
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_log`
--

LOCK TABLES `audit_log` WRITE;
/*!40000 ALTER TABLE `audit_log` DISABLE KEYS */;
INSERT INTO `audit_log` VALUES (1,1,'customer','LOGIN_SUCCESS','username=customer','0:0:0:0:0:0:0:1','2026-06-30 04:34:57',NULL),(2,1,'customer','CHECKOUT','orderId=1','0:0:0:0:0:0:0:1','2026-06-30 04:35:01',NULL),(6,1,'customer','LOGOUT','path=/app/logout','0:0:0:0:0:0:0:1','2026-06-30 04:35:51',NULL),(7,2,'operator','LOGIN_SUCCESS','username=operator','0:0:0:0:0:0:0:1','2026-06-30 04:35:53',NULL),(8,2,'operator','BOOK_SAVE','bookId=10,status=ON','0:0:0:0:0:0:0:1','2026-06-30 04:36:01',NULL),(9,2,'operator','CATEGORY_SAVE','categoryId=6','0:0:0:0:0:0:0:1','2026-06-30 04:36:03',NULL),(10,2,'operator','CATEGORY_DELETE','categoryId=6','0:0:0:0:0:0:0:1','2026-06-30 04:36:03',NULL),(11,2,'operator','ADMIN_VIEW','method=GET,path=/admin/orders/detail','0:0:0:0:0:0:0:1','2026-06-30 04:36:05',NULL),(12,2,'operator','ORDER_SHIP','orderId=1,trackingNoSet=true','0:0:0:0:0:0:0:1','2026-06-30 04:36:06',NULL),(13,2,'operator','ADMIN_VIEW','method=GET,path=/admin/orders/detail','0:0:0:0:0:0:0:1','2026-06-30 04:36:06',NULL),(14,2,'operator','ADMIN_VIEW','method=GET,path=/admin/users','0:0:0:0:0:0:0:1','2026-06-30 04:36:07',NULL),(15,2,'operator','USER_STATUS','targetUserId=1,status=DISABLED','0:0:0:0:0:0:0:1','2026-06-30 04:36:08',NULL),(16,2,'operator','ADMIN_VIEW','method=GET,path=/admin/users','0:0:0:0:0:0:0:1','2026-06-30 04:36:08',NULL),(17,2,'operator','USER_STATUS','targetUserId=1,status=ACTIVE','0:0:0:0:0:0:0:1','2026-06-30 04:36:09',NULL),(18,2,'operator','ADMIN_VIEW','method=GET,path=/admin/users','0:0:0:0:0:0:0:1','2026-06-30 04:36:09',NULL),(19,2,'operator','LOGOUT','path=/app/logout','0:0:0:0:0:0:0:1','2026-06-30 04:37:52',NULL),(20,1,'customer','LOGIN_SUCCESS','username=customer','0:0:0:0:0:0:0:1','2026-06-30 04:37:57',NULL),(21,1,'customer','ORDER_CONFIRM','orderId=1','0:0:0:0:0:0:0:1','2026-06-30 04:38:01',NULL),(22,1,'customer','LOGOUT','path=/app/logout','0:0:0:0:0:0:0:1','2026-06-30 04:38:01',NULL),(23,2,'operator','LOGIN_SUCCESS','username=operator','0:0:0:0:0:0:0:1','2026-06-30 04:38:02',NULL),(24,2,'operator','ADMIN_VIEW','method=GET,path=/admin/orders/detail','0:0:0:0:0:0:0:1','2026-06-30 04:38:04',NULL),(25,2,'operator','ADMIN_VIEW','method=GET,path=/admin/users','0:0:0:0:0:0:0:1','2026-06-30 04:38:05',NULL),(26,2,'operator','LOGOUT','path=/app/logout','0:0:0:0:0:0:0:1','2026-06-30 04:38:08',NULL),(27,4,'auditor','LOGIN_SUCCESS','username=auditor','0:0:0:0:0:0:0:1','2026-06-30 04:38:12',NULL),(28,4,'auditor','AUDIT_VIEW','method=GET,path=/admin/audit','0:0:0:0:0:0:0:1','2026-06-30 04:38:12',NULL),(29,4,'auditor','LOGOUT','path=/app/logout','0:0:0:0:0:0:0:1','2026-06-30 04:38:44',NULL),(30,2,'operator','LOGIN_SUCCESS','username=operator','0:0:0:0:0:0:0:1','2026-06-30 04:38:48',NULL),(31,2,'operator','LOGOUT','path=/app/logout','0:0:0:0:0:0:0:1','2026-06-30 04:39:29',NULL),(32,1,'customer','LOGIN_SUCCESS','username=customer','0:0:0:0:0:0:0:1','2026-06-30 04:39:33',NULL),(33,1,'customer','LOGOUT','path=/app/logout','0:0:0:0:0:0:0:1','2026-06-30 04:39:35',NULL),(34,2,'operator','LOGIN_SUCCESS','username=operator','0:0:0:0:0:0:0:1','2026-06-30 04:39:38',NULL),(35,2,'operator','ADMIN_VIEW','method=GET,path=/admin/orders/detail','0:0:0:0:0:0:0:1','2026-06-30 04:39:42',NULL),(36,2,'operator','ADMIN_VIEW','method=GET,path=/admin/users','0:0:0:0:0:0:0:1','2026-06-30 04:39:42',NULL),(37,2,'operator','LOGOUT','path=/app/logout','0:0:0:0:0:0:0:1','2026-06-30 04:39:45',NULL),(38,4,'auditor','LOGIN_SUCCESS','username=auditor','0:0:0:0:0:0:0:1','2026-06-30 04:39:47',NULL),(39,4,'auditor','AUDIT_VIEW','method=GET,path=/admin/audit','0:0:0:0:0:0:0:1','2026-06-30 04:39:47',NULL),(40,4,'auditor','LOGOUT','path=/app/logout','0:0:0:0:0:0:0:1','2026-06-30 04:41:11',NULL),(41,2,'operator','LOGIN_SUCCESS','username=operator','0:0:0:0:0:0:0:1','2026-06-30 04:41:14',NULL),(42,2,'operator','ADMIN_VIEW','method=GET,path=/admin/users','0:0:0:0:0:0:0:1','2026-06-30 04:41:14',NULL),(43,2,'operator','LOGOUT','path=/app/logout','0:0:0:0:0:0:0:1','2026-06-30 04:42:28',NULL),(44,4,'auditor','LOGIN_SUCCESS','username=auditor','0:0:0:0:0:0:0:1','2026-06-30 04:42:31',NULL),(45,4,'auditor','AUDIT_VIEW','method=GET,path=/admin/audit','0:0:0:0:0:0:0:1','2026-06-30 04:42:31',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (1,'Java核心技术 卷I（原书第12版）','Cay S. Horstmann','机械工业出版社','9787111709473',149.00,48,1,NULL,'Java入门与进阶的经典教程。','ON','2026-06-30 12:32:44'),(2,'深入理解Java虚拟机（第3版）','周志明','机械工业出版社','9787111641247',129.00,30,1,NULL,'JVM 原理与实战。','ON','2026-06-30 12:32:44'),(3,'算法导论（原书第3版）','Thomas H. Cormen','机械工业出版社','9787111407010',128.00,20,1,NULL,'算法领域的权威著作。','ON','2026-06-30 12:32:44'),(4,'活着','余华','作家出版社','9787506365437',45.00,80,2,NULL,'一部关于生命与苦难的小说。','ON','2026-06-30 12:32:44'),(5,'三体（全三册）','刘慈欣','重庆出版社','9787229137939',168.00,60,2,NULL,'中国科幻里程碑之作。','ON','2026-06-30 12:32:44'),(6,'时间简史','史蒂芬·霍金','湖南科学技术出版社','9787535732309',45.00,40,3,NULL,'探索宇宙与时间的本质。','ON','2026-06-30 12:32:44'),(7,'新概念英语（2）','亚历山大、何其莘','外语教学与研究出版社','9787560022086',38.00,100,4,NULL,'经典英语学习教材。','ON','2026-06-30 12:32:44'),(8,'考研数学复习全书','李永乐','中国农业出版社','9787109267701',89.00,25,5,NULL,'考研数学系统复习用书。','ON','2026-06-30 12:32:44'),(9,'已下架示例图书','示例','示例出版社','0000000000000',1.00,0,1,NULL,'该书已下架，用户端不可见。','OFF','2026-06-30 12:32:44'),(10,'Smoke Test Book','QA','Smoke Press','SMOKE-20260630',66.00,12,1,NULL,'Smoke-test book created during final delivery verification.','ON','2026-06-30 12:36:00');
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'计算机',NULL),(2,'文学艺术',NULL),(3,'科技',NULL),(4,'外语',NULL),(5,'考试辅导',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (1,1,1,'Java核心技术 卷I（原书第12版）',149.00,2);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'O2026063012350000016829',1,298.00,'COMPLETED',NULL,'YT202606300001','2026-06-30 04:35:01','2026-06-30 04:35:01','2026-06-30 04:36:06','2026-06-30 04:38:01');
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'customer','da4886d49f5694d5d252baf1d90d8bc02030469eba613ff499d0eb1b6e13ad93','74ae25abc1b9080dcc90715881962b30','张三','M','CMrkDR2GIUqkqPDuFHU02RkvAMXLthXv3AA0gPzcP2c=',NULL,'CUSTOMER','ACTIVE',0,NULL,'2026-06-30 12:32:44','2026-06-30 12:32:44'),(2,'operator','71128fa23502a7d52ecad197b7bfad87312b292632f9870bd111cae62d49afcb','3c8d7cfeface30b4bd4d4b99a2975f4d','运营小李','F','VsxRrmLzs69DA+f70F2/8MpaIhgPZsNItagkx+858g0=',NULL,'OPERATOR_ADMIN','ACTIVE',0,NULL,'2026-06-30 12:32:44','2026-06-30 12:32:44'),(3,'sysadmin','17d736a9a3c8088994a46b40bab10d376e4e5a0420e36c0af1e0f7beb974f224','9e495e1d9d34d27642d0f1db8e9862e7','系统管理员','M','gt42I0mTS2nJaJa1x8syntYwLBguetCbMwVQ82kmjnA=',NULL,'SYSTEM_ADMIN','ACTIVE',0,NULL,'2026-06-30 12:32:44','2026-06-30 12:32:44'),(4,'auditor','57bea300cff3f3f5c048c8ea1d3c72c66c9ad114cffadf985488d8a72e2f2712','f0bea610b392a9be65b6657e87f88557','审计员','F','42BHvTuMi4xNcKZgNrVruLtMV2oOWmSzcRuk6s3wqi8=',NULL,'AUDIT_ADMIN','ACTIVE',0,NULL,'2026-06-30 12:32:44','2026-06-30 12:32:44');
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

-- Dump completed on 2026-06-30 12:49:27
