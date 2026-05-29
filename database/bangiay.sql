-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: bangiay
-- ------------------------------------------------------
-- Server version	8.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admins` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `full_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role` enum('superadmin','manager') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'manager',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
INSERT INTO `admins` VALUES (1,'admin','$2y$10$unp3CUnq9mYTVdBN4xQmdeA3wF7kS8cd40RM7whLQLfRkA6TnlGSa','Quản trị viên chính','superadmin','2025-07-05 14:03:00','2025-07-05 14:53:36'),(2,'manager','$2y$10$yhTUzsrox7PtvVSTGIFmm.Q0bCBPS4FkfpYp8qCXhC1G5hafln1hy','Quản lý cửa hàng hàng','manager','2025-07-05 14:03:00','2026-05-17 16:12:05');
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_banners`
--

DROP TABLE IF EXISTS `app_banners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_banners` (
  `id` int NOT NULL AUTO_INCREMENT,
  `image_url` text NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `display_order` int DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_banners`
--

LOCK TABLES `app_banners` WRITE;
/*!40000 ALTER TABLE `app_banners` DISABLE KEYS */;
INSERT INTO `app_banners` VALUES (1,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/banners%2F1780041240894_banner4.jpg?alt=media&token=502f1de0-5f55-4281-9508-1b86e69fec8a','',1,3,'2026-05-28 12:43:18'),(2,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/banners%2F1779972437437_banner2.jpg?alt=media&token=81b3088b-0185-4a4a-90cb-ba82563ba3ef','',1,2,'2026-05-28 12:47:18'),(3,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/banners%2F1779972473031_banner1.jpeg?alt=media&token=a5ffff87-a1e1-4880-94e9-767e2a31022c','',1,1,'2026-05-28 12:47:53');
/*!40000 ALTER TABLE `app_banners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `color` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `size` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Chạy bộ',NULL,'2026-04-12 04:42:37'),(2,'Công sở',NULL,'2026-04-12 04:42:37'),(3,'Sneaker',NULL,'2026-04-12 04:42:37'),(4,'Thể thao',NULL,'2026-04-12 04:42:37');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fcm_tokens`
--

DROP TABLE IF EXISTS `fcm_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fcm_tokens` (
  `id` int NOT NULL AUTO_INCREMENT,
  `firebase_uid` varchar(128) NOT NULL,
  `token` text NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fcm_tokens`
--

LOCK TABLES `fcm_tokens` WRITE;
/*!40000 ALTER TABLE `fcm_tokens` DISABLE KEYS */;
INSERT INTO `fcm_tokens` VALUES (1,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','d0-1cumIRRuC-A3tCdHo1k:APA91bE0Q7OyEpcaLmjBU33H25PqaPPmKSbm-J0AEUHYgbuCE3rBrOzIaMBDBzWvxlpZXwtF_LqYfPZAttyp0mOyvXR4oS_HkCiLu4yuWVQey1yiCIx_TnA','2026-05-29 08:58:15','2026-05-29 08:58:15'),(2,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','f53a_bOkQGSmCBznCyuivN:APA91bExVmf82UHx5U3Gf-f7I585gikDFJfpJStHp610RZWFopGAkkmeQQRmz82_rzNqiWvscXsfu95T86px6BOMjlvjWAXyeyMB_mbtwKHk0oqcEkqpP2A','2026-05-29 10:07:34','2026-05-29 10:07:34');
/*!40000 ALTER TABLE `fcm_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` int NOT NULL AUTO_INCREMENT,
  `firebase_uid` varchar(128) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `message` text NOT NULL,
  `type` varchar(50) DEFAULT 'system',
  `related_order_id` int DEFAULT NULL,
  `is_read` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Siêu sale đến rồi khách iu ơi','Siêu sale đến rồi khách iu ơi, tận hưởng quà ngập tràn với shop nhé','promotion',NULL,1,'2026-05-28 19:43:13'),(2,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Đơn hàng hoàn thành','Đơn hàng #46 đã hoàn thành. Cảm ơn bạn đã mua hàng tại HoangShoes.','order',46,1,'2026-05-28 19:43:48'),(3,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Đơn hàng đang xử lý','Đơn hàng #41 của bạn đang được xử lý.','order',41,1,'2026-05-28 19:45:43'),(4,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Đơn hàng đang giao','Đơn hàng #40 đang trên đường giao đến bạn.','order',40,1,'2026-05-28 19:56:58'),(5,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Đơn hàng đang giao','Đơn hàng #39 đang trên đường giao đến bạn.','order',39,1,'2026-05-28 20:03:31'),(6,NULL,'Sale bạt ngàn','Vui khỏe sống sale','promotion',NULL,1,'2026-05-28 20:04:20'),(7,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','SALE','Nội dung thông báo Nội dung thông báo Nội dung thông báo','promotion',NULL,1,'2026-05-29 08:47:07'),(8,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Đơn hàng đang xử lý','Đơn hàng #38 của bạn đang được xử lý.','order',38,1,'2026-05-29 08:48:24'),(9,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Đơn hàng đang giao','Đơn hàng #38 đang trên đường giao đến bạn.','order',38,1,'2026-05-29 08:49:17'),(10,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Đơn hàng đã hủy','Đơn hàng #38 đã bị hủy.','order',38,1,'2026-05-29 08:50:40'),(11,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Đơn hàng đang xử lý','Đơn hàng #47 của bạn đang được xử lý.','order',47,1,'2026-05-29 09:16:05'),(12,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Đơn hàng đang giao','Đơn hàng #47 đang trên đường giao đến bạn.','order',47,1,'2026-05-29 09:17:13'),(13,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Đơn hàng đã hủy','Đơn hàng #47 đã bị hủy.','order',47,1,'2026-05-29 09:23:38'),(14,NULL,'Sale này bạn iu ơi','Siêu sale ngập tràn','promotion',NULL,1,'2026-05-29 09:24:39'),(15,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Đơn hàng đang xử lý','Đơn hàng #47 của bạn đang được xử lý.','order',47,1,'2026-05-29 09:30:27');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `color` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `size` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,1,1,3,1200000.00,NULL,NULL),(2,1,3,2,1500000.00,NULL,NULL),(3,1,2,1,950000.00,NULL,NULL),(4,2,1,3,1200000.00,NULL,NULL),(5,2,3,4,1500000.00,NULL,NULL),(6,3,1,1,1200000.00,NULL,NULL),(7,3,2,1,950000.00,NULL,NULL),(8,4,2,1,950000.00,NULL,NULL),(9,5,2,1,950000.00,NULL,NULL),(10,6,1,1,1200000.00,NULL,NULL),(11,7,2,1,950000.00,NULL,NULL),(12,8,1,1,1200000.00,NULL,NULL),(13,9,2,1,950000.00,NULL,NULL),(14,10,3,1,1500000.00,NULL,NULL),(15,11,1,1,1200000.00,NULL,NULL),(16,12,1,1,1200000.00,NULL,NULL),(18,14,1,1,1200000.00,NULL,NULL),(19,14,1,1,1200000.00,NULL,NULL),(21,15,1,1,1200000.00,NULL,NULL),(22,16,1,1,1200000.00,NULL,NULL),(23,17,1,1,3239000.00,NULL,NULL),(24,17,3,1,995000.00,NULL,NULL),(25,18,10,1,890000.00,NULL,NULL),(27,20,3,1,995000.00,NULL,NULL),(28,20,7,1,2349000.00,NULL,NULL),(29,20,14,1,2600000.00,NULL,NULL),(30,20,21,1,800000.00,NULL,NULL),(31,21,1,1,10000.00,'Đen','40'),(32,22,2,1,2649000.00,'Đen Logo Trắng','40'),(33,23,1,1,10000.00,'Đen','40'),(34,24,1,1,10000.00,'Đen','41'),(35,25,2,1,2649000.00,'Đen Logo Trắng','40'),(36,25,3,1,995000.00,'Trắng Logo Đen','38'),(37,26,1,1,10000.00,'Đen','41'),(38,27,1,1,10000.00,'Đen','41'),(39,28,1,1,10000.00,'Đen','41'),(40,29,1,1,10000.00,'Đen','41'),(41,30,1,1,10000.00,'Đen','41'),(42,31,1,1,10000.00,'Đen','41'),(43,32,1,1,10000.00,'Đen','41'),(44,33,1,1,10000.00,'Đen','41'),(45,34,1,1,10000.00,'Đen','41'),(46,35,1,1,10000.00,'Đen','41'),(47,36,1,1,10000.00,'Đen','41'),(48,37,1,1,10000.00,'Đen','41'),(49,38,1,1,10000.00,'Đen','41'),(50,39,1,1,10000.00,'Đen','41'),(51,40,1,1,10000.00,'Đen','41'),(52,41,1,1,10000.00,'Đen','41'),(53,42,1,1,10000.00,'Đen','41'),(54,43,1,1,10000.00,'Đen','41'),(55,44,2,1,2649000.00,'Đen Logo Trắng','41'),(56,45,3,1,995000.00,'Trắng Logo Đen','37'),(57,46,1,1,10000.00,'Đen','41'),(58,47,1,1,10000.00,'Đen','41'),(59,47,2,1,2649000.00,'Đen Logo Trắng','40'),(60,47,3,1,995000.00,'Trắng Logo Đen','37');
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'pending',
  `shipping_address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `firebase_uid` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `customer_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `customer_email` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `note` text COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,1,7550000.00,'pending','Root','Bank Transfer','2025-07-04 09:53:06','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(2,1,9600000.00,'pending','QS','COD','2025-07-05 04:41:58','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(3,1,2150000.00,'pending','Root','COD','2025-07-05 08:48:34','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(4,1,950000.00,'pending','Root','COD','2025-07-05 08:49:14','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(5,1,950000.00,'pending','Root','Bank Transfer','2025-07-05 08:49:59','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(6,1,1200000.00,'pending','Root','COD','2025-07-05 08:56:15','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(7,1,950000.00,'pending','Root','Bank Transfer','2025-07-05 08:56:29','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(8,1,1200000.00,'pending','Root','COD','2025-07-05 08:57:44','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(9,1,950000.00,'pending','Root','COD','2025-07-05 08:58:44','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(10,1,1500000.00,'pending','Root','COD','2025-07-05 09:01:49','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(11,1,1200000.00,'pending','Root','Bank Transfer','2025-07-05 09:01:57','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(12,1,1200000.00,'pending','Root','Bank Transfer','2025-07-05 09:02:04','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(13,1,1350000.00,'pending','Root','COD','2025-07-05 09:03:17','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(14,1,3750000.00,'pending','Root','COD','2025-07-05 14:29:40','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(15,1,1200000.00,'pending','Root','Bank Transfer','2025-07-06 07:17:50','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(16,1,1200000.00,'pending','Root','Bank Transfer','2025-07-06 07:18:20','2026-05-18 09:06:38',NULL,NULL,NULL,NULL),(17,6,4234000.00,'cancelled','Phường 13, Tân Bình','Bank Transfer','2025-07-06 13:06:40','2026-05-19 14:00:37',NULL,NULL,NULL,NULL),(18,6,890000.00,'completed','Phường 13, Tân Bình, Hồ Chí Minh','COD','2025-07-06 16:03:06','2026-05-19 14:00:29',NULL,NULL,NULL,NULL),(19,7,6179000.00,'shipped','Hà Nội','Bank Transfer','2025-07-06 16:11:15','2026-05-19 14:00:17',NULL,NULL,NULL,NULL),(20,7,6744000.00,'processing','Hà Nội','Bank Transfer','2025-07-06 17:28:09','2026-05-19 14:00:09',NULL,NULL,NULL,NULL),(21,1,10000.00,'completed','123 - 123, 123','COD','2026-05-27 17:21:43','2026-05-27 17:24:28',NULL,NULL,NULL,NULL),(22,1,2649000.00,'completed','123 - 123, 123','COD','2026-05-27 17:37:40','2026-05-27 17:38:19','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(23,1,10000.00,'completed','123 - 123, 123','COD','2026-05-27 17:47:55','2026-05-27 17:48:56','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(24,1,10000.00,'pending','123 - 123, 123','COD','2026-05-27 18:36:56','2026-05-27 18:36:56','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(25,1,3644000.00,'pending','123 - 123, 123','COD','2026-05-27 18:38:02','2026-05-27 18:38:02','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(26,1,10000.00,'pending','123 - 123, 123','COD','2026-05-27 18:40:49','2026-05-27 18:40:49','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(27,1,10000.00,'pending','123 - 123, 123','COD','2026-05-28 07:24:24','2026-05-28 07:24:24','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(28,1,10000.00,'pending','123 - 123, 123','VISA','2026-05-28 07:24:39','2026-05-28 07:24:39','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(29,1,10000.00,'pending','123 - 123, 123','COD','2026-05-28 07:51:14','2026-05-28 07:51:14','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(30,1,10000.00,'pending','123 - 123, 123','COD','2026-05-28 08:11:19','2026-05-28 08:11:19','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(31,1,10000.00,'pending','123 - 123, 123','COD','2026-05-28 08:13:40','2026-05-28 08:13:40','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(32,1,10000.00,'pending','123 - 123, 123','COD','2026-05-28 08:26:16','2026-05-28 08:26:16','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(33,1,10000.00,'pending','123 - 123, 123','BANK_TRANSFER','2026-05-28 08:33:33','2026-05-28 08:33:33','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(34,1,10000.00,'pending','123 - 123, 123','BANK_TRANSFER','2026-05-28 08:36:35','2026-05-28 08:36:35','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(35,1,10000.00,'pending','123 - 123, 123','BANK_TRANSFER','2026-05-28 08:46:28','2026-05-28 08:46:28','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(36,1,10000.00,'pending','123 - 123, 123','BANK_TRANSFER','2026-05-28 08:47:36','2026-05-28 08:47:36','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(37,1,10000.00,'pending','123 - 123, 123','BANK_TRANSFER','2026-05-28 08:48:09','2026-05-28 08:48:09','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(38,1,10000.00,'cancelled','123 - 123, 123','BANK_TRANSFER','2026-05-28 08:48:31','2026-05-29 08:50:40','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(39,1,10000.00,'shipped','123 - 123, 123','BANK_TRANSFER','2026-05-28 08:54:37','2026-05-28 20:03:31','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(40,1,10000.00,'shipped','123 - 123, 123','COD','2026-05-28 08:55:22','2026-05-28 19:56:58','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',NULL),(41,1,10000.00,'processing','123 - 123, 123','COD','2026-05-28 09:08:16','2026-05-28 19:45:43','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com','Ahiiiiiiiiii'),(42,1,10000.00,'processing','123 - 123, 123','BANK_TRANSFER','2026-05-28 09:09:42','2026-05-28 12:38:12','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',''),(43,1,10000.00,'completed','123 - 123, 123','COD','2026-05-28 14:00:56','2026-05-28 14:01:06','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',''),(44,1,2649000.00,'completed','123 - 123, 123','COD','2026-05-28 14:02:16','2026-05-28 14:02:24','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',''),(45,1,995000.00,'completed','123 - 123, 123','COD','2026-05-28 14:03:39','2026-05-28 14:03:48','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',''),(46,1,10000.00,'completed','123 - 123, 123','COD','2026-05-28 14:36:26','2026-05-28 19:43:48','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com',''),(47,1,3654000.00,'processing','123 - 123, 123','COD','2026-05-28 15:01:36','2026-05-29 09:30:27','1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang','hoang@gmail.com','');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_images`
--

DROP TABLE IF EXISTS `product_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_images` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `image_url` varchar(1000) NOT NULL,
  `sort_order` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `product_images_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_images`
--

LOCK TABLES `product_images` WRITE;
/*!40000 ALTER TABLE `product_images` DISABLE KEYS */;
INSERT INTO `product_images` VALUES (65,1,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2Fextra%2F1779729312081_2_Pegasus%20Premium.avif?alt=media&token=15f15310-9d47-4f2b-a5bc-3be6f216333a',2),(66,1,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2Fextra%2F1779729311202_1_nikeair1.avif?alt=media&token=9e8f8cd8-dddf-4224-889b-d16ba4a2ceb3',1),(67,1,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2Fextra%2F1779729310634_0_Nike%20Interact%20Run.png?alt=media&token=c7810310-7e18-4d85-b03c-9968d347a9de',2);
/*!40000 ALTER TABLE `product_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_reviews`
--

DROP TABLE IF EXISTS `product_reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_reviews` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `firebase_uid` varchar(128) NOT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `rating` int NOT NULL,
  `comment` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_hidden` tinyint(1) DEFAULT '0',
  `admin_reply` text,
  `admin_reply_at` datetime DEFAULT NULL,
  `review_image` varchar(500) DEFAULT NULL,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `avatar_url` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_product_review` (`product_id`,`firebase_uid`),
  CONSTRAINT `product_reviews_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_reviews`
--

LOCK TABLES `product_reviews` WRITE;
/*!40000 ALTER TABLE `product_reviews` DISABLE KEYS */;
INSERT INTO `product_reviews` VALUES (1,1,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang',5,'123456','2026-05-28 01:46:19',0,'Ahi hi','2026-05-28 13:02:06',NULL,'2026-05-28 20:02:05',NULL),(2,2,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Cong Hoang',5,'Giay Dep Lam','2026-05-28 21:02:48',0,NULL,NULL,NULL,'2026-05-28 21:02:48','https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/avatars%2F1Bd9l6OnjSaQhgk1qrLw1EdvLwU2.jpg?alt=media&token=2dfd3b5d-db90-404b-805e-0bae992c8ae8'),(3,3,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','Nguyen Thuy',5,'Giay qua dep shop oi','2026-05-28 21:04:12',0,NULL,NULL,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/reviews%2Fe5560ce3-bc64-4fe8-8cd9-720761f7011a.jpg?alt=media&token=357321c0-ef41-474f-b3e0-06aa5bbdccad','2026-05-28 21:04:12','https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/avatars%2F1Bd9l6OnjSaQhgk1qrLw1EdvLwU2.jpg?alt=media&token=2dfd3b5d-db90-404b-805e-0bae992c8ae8');
/*!40000 ALTER TABLE `product_reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_variants`
--

DROP TABLE IF EXISTS `product_variants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_variants` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `color` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `size` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `stock_quantity` int DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `product_variants_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=262 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_variants`
--

LOCK TABLES `product_variants` WRITE;
/*!40000 ALTER TABLE `product_variants` DISABLE KEYS */;
INSERT INTO `product_variants` VALUES (25,2,'Đen Logo Trắng','40',10,'2025-07-06 07:52:42','2025-07-06 07:52:42'),(26,2,'Trắng Logo Đen','40',11,'2025-07-06 07:53:19','2025-07-06 07:53:19'),(27,2,'Đen Logo Trắng','41',4,'2025-07-06 07:53:49','2026-05-28 14:02:24'),(28,2,'Trắng Logo Đen','41',7,'2025-07-06 07:54:06','2025-07-06 07:54:06'),(29,3,'Trắng Logo Đen','37',6,'2025-07-06 08:01:22','2026-05-28 14:03:48'),(30,3,'Trắng Logo Đen','38',9,'2025-07-06 08:01:41','2025-07-06 08:01:41'),(31,4,'Trắng','35',6,'2025-07-06 08:04:29','2025-07-06 08:04:29'),(32,4,'Trắng','36',7,'2025-07-06 08:04:41','2025-07-06 08:04:41'),(33,5,'Đen Logo Trắng','43',10,'2025-07-06 08:09:51','2025-07-06 08:09:51'),(34,5,'Đen Logo Trắng','43',9,'2025-07-06 08:10:05','2025-07-06 08:10:05'),(35,6,'Trắng','40',3,'2025-07-06 08:14:39','2025-07-06 08:14:39'),(36,6,'Trắng','37',2,'2025-07-06 08:14:59','2025-07-06 08:14:59'),(37,7,'Đen Logo Trắng','39',5,'2025-07-06 08:17:21','2025-07-06 08:17:21'),(38,7,'Đen Logo Trắng','38',5,'2025-07-06 08:18:40','2025-07-06 08:18:40'),(39,8,'Trắng Logo Đen','41',6,'2025-07-06 08:21:10','2025-07-06 08:21:10'),(40,8,'Trắng Logo Đen','39',2,'2025-07-06 08:21:26','2025-07-06 08:21:26'),(41,9,'Đen Nâu','40',12,'2025-07-06 08:25:20','2025-07-06 08:25:20'),(42,9,'Đen Xám','40',3,'2025-07-06 08:25:40','2025-07-06 08:25:40'),(43,11,'Trắng','40',10,'2025-07-06 08:28:12','2025-07-06 08:28:12'),(44,10,'Trắng','40',10,'2025-07-06 08:28:30','2025-07-06 08:28:30'),(45,10,'Trắng','41',3,'2025-07-06 08:28:40','2025-07-06 08:28:40'),(46,11,'Trắng Logo Đen','40',3,'2025-07-06 08:32:17','2025-07-06 08:32:17'),(47,12,'Trắng Logo Đen','41',4,'2025-07-06 08:35:12','2025-07-06 08:35:12'),(48,12,'Trắng Logo Đen','40',4,'2025-07-06 08:35:33','2025-07-06 08:35:33'),(49,14,'Trắng/Xanh Dương','40',5,'2025-07-06 08:37:47','2025-07-06 08:37:47'),(50,14,'Trắng/Hồng','40',5,'2025-07-06 08:38:09','2025-07-06 08:38:09'),(51,15,'Nâu/Xám','40',10,'2025-07-06 08:45:29','2025-07-06 08:45:29'),(52,15,'Nâu/Đen','40',5,'2025-07-06 08:45:50','2025-07-06 08:45:50'),(53,16,'Xám Logo Đen','40',5,'2025-07-06 08:49:14','2025-07-06 08:49:14'),(54,16,'Xám Logo Đen','41',5,'2025-07-06 08:49:31','2025-07-06 08:49:31'),(55,17,'Đen/Xám','40',6,'2025-07-06 08:56:10','2025-07-06 08:56:10'),(56,17,'Đen/Xám','41',10,'2025-07-06 08:56:25','2025-07-06 08:56:25'),(57,18,'Đen','37',4,'2025-07-06 08:57:48','2025-07-06 08:57:48'),(58,18,'Đen','40',5,'2025-07-06 08:58:01','2025-07-06 08:58:01'),(65,23,'Trắng/Đen/Xám khói/Nho dại','41',13,'2025-07-06 12:52:08','2025-07-06 12:52:08'),(67,24,'Trắng/Đen/Bạc kim loại','41',5,'2025-07-06 12:54:54','2025-07-06 12:54:54'),(80,25,'Xanh lá cây nhạt/Xốp bạc hà/Bạc kim loại/Đỏ thẫm s','42',10,'2026-05-22 03:27:37','2026-05-22 03:27:37'),(83,20,'Đen','43',16,'2026-05-22 03:32:11','2026-05-22 03:32:11'),(84,20,'Nâu','40',5,'2026-05-22 03:32:11','2026-05-22 03:32:11'),(85,21,'Nâu','41',6,'2026-05-22 03:33:11','2026-05-22 03:33:11'),(86,21,'Nâu','42',6,'2026-05-22 03:33:11','2026-05-22 03:33:11'),(87,22,'Đen','40',3,'2026-05-22 03:34:23','2026-05-22 03:34:23'),(88,22,'Đen','42',3,'2026-05-22 03:34:23','2026-05-22 03:34:23'),(97,19,'Đen','41',5,'2026-05-23 16:58:08','2026-05-23 16:58:08'),(98,19,'Nâu Đậm','40',10,'2026-05-23 16:58:08','2026-05-23 16:58:08'),(259,1,'Đen','40',0,'2026-05-28 09:42:33','2026-05-28 09:42:33'),(260,1,'Đen','41',4,'2026-05-28 09:42:33','2026-05-28 19:43:48'),(261,1,'Trắng','42',5,'2026-05-28 09:42:33','2026-05-28 09:42:33');
/*!40000 ALTER TABLE `product_variants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `price` decimal(10,2) NOT NULL,
  `image` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `brand` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `color` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `stock_quantity` int DEFAULT '0',
  `material` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `gender` enum('Nam','Nữ','Unisex') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'Unisex',
  `season` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `style` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `original_price` decimal(10,2) DEFAULT '0.00',
  `discount_percent` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `category` (`category`),
  KEY `brand` (`brand`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Nike Dunk Hight','Ban đầu được tạo ra cho sàn gỗ cứng, Dunk sau đó đã xuất hiện trên đường phố—và, như người ta nói, phần còn lại là lịch sử. Hơn 35 năm sau khi ra mắt, hình bóng này vẫn mang phong cách táo bạo, thách thức và vẫn là diện mạo được các đội bóng thèm muốn cả trong và ngoài sân đấu. \n\nGiờ đây, OG bóng rổ đại học trở lại với khối màu lấy cảm hứng từ di sản. Sự kết hợp cổ điển giữa màu trắng và Game Royal mang đến cho kiểu trang điểm này cảm giác hoài cổ, với công nghệ giày dép hiện đại đưa sự thoải mái của thiết kế vào thế kỷ 21.',10000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779812187734_Nike%20Dunk%20Hight.png?alt=media&token=fab5be94-5660-4f50-9e3b-4f001f154b7e','Sneaker',NULL,'Nike','2025-06-26 05:42:10','2026-05-28 09:42:33','Xanh Dương',46,'Vải lưới','Unisex','Hè','Thể thao',1,15000.00,33),(2,'Nike Air Max Nuaxis','Thoáng khí và thoải mái, Air Max Nuaxis đã sẵn sàng cho cuộc sống hàng ngày. Bạn có thể tin tưởng rằng nó sẽ trông đẹp và cũng cảm thấy thoải mái. Nó kết hợp một đơn vị Air trực quan với các yếu tố thiết kế lấy cảm hứng từ Air Max 270, khiến nó trở thành đôi giày hoàn hảo để buộc dây và đi.\n\nNhững lợi ích\nPhần lưới phía trên giúp bạn luôn mát mẻ khi cuộc sống bận rộn.\nMềm mại và thoải mái, đệm Max Air có độ nâng đỡ vừa phải.\nĐế ngoài bằng cao su mang lại cho bạn lực kéo bền bỉ.\nChi tiết sản phẩm\nMàu sắc hiển thị: Đen/Đen/Trắng/Trắng\nKiểu dáng: FD4329-001\nQuốc gia/Khu vực xuất xứ: Ấn Độ, Việt Nam\nĐơn vị Max Air\nVẻ đẹp ngoạn mục. Cửa sổ nhìn ra đế giày. Nhà thiết kế huyền thoại Tinker Hatfield đã lấy cảm hứng từ kiến ​​trúc Paris từ trong ra ngoài để tái lập công nghệ Air như một công nghệ hàng đầu về đệm. Mềm mại và thoải mái, bộ phận Max Air có độ nâng đỡ vừa phải.',2649000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779208888492_Nike%20Air%20Max%20Nuaxis.png?alt=media&token=73d24fad-7064-444c-876f-e8dd43b03eb0','Chạy bộ',1,'Nike','2025-06-26 05:42:10','2026-05-25 13:47:50','Đen',33,'','Nam','','',1,2649000.00,0),(3,'Nike Blazer Vintage','PHONG CÁCH CỔ ĐIỂN.\n\nVào những năm 70, Nike là đôi giày mới trên thị trường. Thực tế là mới đến mức chúng tôi vẫn đang đột phá vào làng bóng rổ và thử nghiệm các mẫu giày trên chân của đội bóng địa phương. Tất nhiên, thiết kế đã được cải thiện qua nhiều năm, nhưng cái tên vẫn được giữ nguyên. Nike Blazer Mid \'77 Vintage—cổ điển ngay từ đầu.\n\nNhững lợi ích\nPhần trên bằng da và chất liệu tổng hợp vẫn giữ được vẻ cổ điển của bản gốc đồng thời tăng thêm sự thoải mái và hỗ trợ.\nPhần đế giữa được xử lý theo phong cách cổ điển mang đến vẻ ngoài cổ điển.\nKết cấu hấp khử trùng kết hợp đế ngoài với đế giữa để tạo nên vẻ ngoài hợp lý gợi nhớ đến thiết kế của thập niên 70.\nLớp bọt lộ ra trên lưỡi gà mang lại vẻ đẹp hoài cổ.\nĐế ngoài bằng cao su đặc, không để lại dấu vết vẫn có cùng họa tiết xương cá ngay từ đầu, tại sao phải thay đổi những gì hiệu quả? Độ bám đường và độ bền tuyệt vời giúp để lại dấu ấn mà bạn yêu thích.\nChi tiết sản phẩm\nMàu sắc hiển thị: Trắng/Đen\nKiểu dáng: BQ6806-100\nQuốc gia/Khu vực xuất xứ: Indonesia, Ấn Độ, Việt Nam',995000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779208910069_Nike%20Blazer%20Vintage.png?alt=media&token=0d8ad27f-ebca-4913-8df8-f47541670575','Sneaker',3,'Nike','2025-06-26 05:42:10','2026-05-25 13:47:50','Đen/Trắng',16,'Vải lưới','Unisex','Thu-Đông','Sneaker',1,995000.00,0),(4,'Nike Air Force 1 \'07','Thoải mái, bền bỉ và vượt thời gian—nó là số một vì một lý do. Cấu trúc cổ điển của thập niên 80 được kết hợp với logo Swoosh phồng và lớp phủ kim loại để tạo nên phong cách theo dõi dù bạn đang ở trên sân hay đang di chuyển.\n\nNhững lợi ích\nPhần trên bằng da tổng hợp với phần mũi giày đục lỗ thoáng khí và thoải mái.\nĐược thiết kế ban đầu cho mục đích chơi bóng rổ, đệm Nike Air mang lại sự thoải mái nhẹ nhàng suốt cả ngày.\nĐế ngoài bằng cao su với các vòng tròn xoay truyền thống mang lại lực kéo và độ bền.\nChi tiết sản phẩm\n2 bộ dây giày\nCổ áo có đệm\nĐế giữa bằng bọt\nMàu sắc hiển thị: Trắng/Đen/Vàng kim loại/Trắng\nKiểu dáng: HF2014-100\nQuốc gia/Khu vực xuất xứ: Việt Nam\nKhông lực 1\nRa mắt vào năm 1982 như một đôi giày bóng rổ phải có, Air Force 1 đã trở nên nổi tiếng vào những năm 90. Vẻ ngoài sạch sẽ của đôi AF-1 trắng-trên-trắng cổ điển đã được chứng thực từ sân bóng rổ đến đường phố và hơn thế nữa. Tìm thấy nhịp điệu của mình trong văn hóa hip-hop, phát hành các bản phối lại và phối màu giới hạn, Air Force 1 đã trở thành một đôi giày thể thao mang tính biểu tượng trên toàn cầu. Và với hơn 2.000 lần lặp lại của mẫu giày chủ lực này, tác động của nó đối với thời trang, âm nhạc và văn hóa giày thể thao là không thể phủ nhận.',2815000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779208920645_Nike%20Air%20Force%201%20\'07.png?alt=media&token=d4aab0d5-36b7-4298-81d5-24f6b8546fec','Sneaker',3,'Nike','2025-06-26 05:49:11','2026-05-25 13:47:50','',13,'','Unisex','','',1,2815000.00,0),(5,'Nike Interact Run','Bạn có thể thấy tương lai không? Hãy nhanh chóng tiến về phía trước trong đôi Nike Interact Run tiên tiến. Nó được thiết lập với tất cả những điều tuyệt vời khi chạy mà bạn cần: phần trên Flyknit nhẹ, đế giữa bằng bọt mềm và sự thoải mái khi cần thiết. Quét mã QR trên lưỡi giày bằng điện thoại của bạn và xem phần giới thiệu trực tuyến của chúng tôi về những điều cần biết về Nike Interact Run. Thêm vào đó, phần EasyOn không dây có gót uốn cong để vừa vặn rảnh tay và Flyknit co giãn để phù hợp với mọi loại bàn chân.\n\n\nMàu sắc hiển thị: Đen/Đen/Trắng\nKiểu dáng: FV5590-001\nQuốc gia/Khu vực xuất xứ: Việt Nam',1879000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779208936380_Nike%20Interact%20Run.png?alt=media&token=bd189105-9e6f-476a-8086-22d2a6684c50','Thể thao',4,'Nike','2025-06-26 05:49:11','2026-05-25 13:47:50','Đen/Trắng',19,'','Unisex','','',1,1879000.00,0),(6,'Nike Air Force 1 Mid','Những đôi AF-1 này đều là về các chi tiết. Tùy chỉnh nhãn, dây giày và dubrae của bạn, và đừng quên để lại dấu ấn của bạn bằng văn bản cá nhân trên tab sau. Với 8 lựa chọn màu sắc và các tùy chọn cao su trong suốt và cao su tổng hợp bổ sung cho đế, thiết kế này chắc chắn sẽ trở thành duy nhất—giống như bạn vậy.\n\n\nKiểu dáng: HF0660-900',4109000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779208946380_Nike%20Air%20Force%201%20Mid.png?alt=media&token=0fbd77a0-1cc0-4339-bbf8-d8498075fda1','Sneaker',3,'Nike','2025-06-26 05:49:11','2026-05-25 13:47:50','',5,'','Unisex','','',1,4109000.00,0),(7,'Nike Air Max INT','Bạn phải cảm nhận được cảm giác để trở thành cảm giác. Hãy đến với Air Max INTRLK. Đệm được thiết kế lại mang đến sự thoải mái với khả năng phản hồi tăng lên và độ nảy hoàn hảo. Vật liệu nhẹ, dễ tạo kiểu có thể chịu được sự hao mòn. Hoàn thiện hơn nữa, đế ngoài lấy cảm hứng từ Waffle mang đến sự hấp dẫn thực sự của Nike.\n\n\nMàu sắc hiển thị: Đen/Trắng\nKiểu dáng: DX3705-001\nQuốc gia/Khu vực xuất xứ: Indonesia',2349000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779208954988_Nike%20Air%20Max%20INT.png?alt=media&token=7bb5318d-010d-4b50-9195-5c8065be999d','Chạy bộ',1,'Nike','2025-06-26 05:49:11','2026-05-25 13:47:50','',10,'','Unisex','','',1,2349000.00,0),(8,'Nike Court Vision','Giày Thể Thao Nữ NIKE Nike Court Vision Low Next Nature DH3158-101\n\n100% chính hãng NIKE Việt Nam\n\nBao gồm: Sản phẩm mới nguyên TAG, hóa đơn bán hàng từ Shoestore',1982000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779208967483_Nike%20Court%20Vision.png?alt=media&token=4601e9e5-04d4-4c16-b729-cc4d9e9b1dc9','Sneaker',3,'Nike','2025-06-26 05:49:11','2026-05-25 13:47:50','',8,'','Unisex','','',1,1982000.00,0),(9,'Nike Air Max Plus','Mua Giày Nike Air Max Plus ‘Black University Gold’ DM0032-013 chính hãng 100%. Giao hàng miễn phí trong 1 ngày khi thanh toán đầy đủ tổng giá trị đơn hàng. Cam kết đền tiền X5 nếu phát hiện Fake. Đổi trả miễn phí size. FREE vệ sinh giày trọn đời.',5900000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779208981403_Nike%20Air%20Max%20Plus.png?alt=media&token=da13c0e0-fd74-42b1-b7ee-fd52eb5a2273','Chạy bộ',1,'Nike','2025-06-26 05:49:11','2026-05-25 13:47:50','',15,'','Unisex','','',1,5900000.00,0),(10,'Nike Air Max 97','AM97 là đôi giày có thể thay đổi hình dạng vào thời điểm đó, và giờ đến lượt bạn làm điều tương tự. Tùy chỉnh mọi bộ phận của giày từ vật liệu trên cùng đến màu sắc của đế giữa và bộ phận Nike Air, cùng với dây giày chống trượt để đảm bảo vừa vặn. Sau đó, quyết định xem bạn muốn đế ngoài của mình là đế đặc, có màu hay trong suốt. Thậm chí còn có một đế trong được nâng cấp để tăng thêm độ đệm dưới chân. Cuối cùng, một đôi giày đa năng như chính bạn.\n\nHiển thị: Nhiều màu/Nhiều màu/Nhiều màu\nKiểu dáng: FN6743-900\nQuốc gia/Khu vực xuất xứ: Việt Nam',890000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779208989764_Nike%20Air%20Max%2097.png?alt=media&token=ba487bb9-758a-4006-832b-a41aaf39124c','Chạy bộ',1,'Nike','2025-06-26 05:49:11','2026-05-25 13:47:50','',13,'','Unisex','','',1,890000.00,0),(11,'Adidas Superstar','Đôi giày adidas classic với các điểm nhấn đính hạt.\nDòng giày adidas Superstar luôn thể hiện tinh thần thể thao với cá tính nổi loạn. Mang đến nét mới mẻ cho biểu tượng ấy, đôi giày trainer này nổi bật với 3 Sọc đính hạt. Mũi giày vỏ sò bằng cao su độc đáo mang đến phong cách đặc trưng, như đã có hơn 50 năm. Bất kể bạn đang xuống phố hay thư giãn, đôi giày cổ thấp này sẽ giúp bạn luôn thoải mái suốt ngày dài.',2600000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779209010236_Adidas%20Superstar.png?alt=media&token=05a135f3-f10d-451b-86e2-4608ade4ecd4','Sneaker',3,'Adidas','2025-06-26 05:49:11','2026-05-25 13:47:50','',13,'','Unisex','','',1,2600000.00,0),(12,'Adidas Samba','Đôi giày Samba đích thực dành cho thế hệ mới.\nNếu quay ngược thời gian trở về thập niên 1960, bạn sẽ thấy các cầu thủ bóng đá chuyên nghiệp mang những đôi giày adidas Samba giống hệt như đôi giày trẻ em này. Hoặc gần giống. Với thiết kế cải biên dành cho thế hệ mới, phiên bản này có lót giày mềm mại cho cảm giác thoải mái suốt ngày dài vui chơi. Thân giày bằng da, đế gum và 3 Sọc đậm chất classic.',1400000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779209019766_Adidas%20Samba.png?alt=media&token=e8ff92e3-ea20-4a3e-9b76-54dc41377a17','Sneaker',3,'Adidas','2025-06-26 05:49:11','2026-05-25 13:47:50','',8,'','Unisex','','',1,1400000.00,0),(14,'Adidas Forum Low','Giày cổ thấp kiểu dáng classic với các chi tiết táo bạo.\nSự kết hợp giữa phong cách classic và hiện đại, đôi giày adidas Forum Low mang đến sự hòa quyện giữa các yếu tố đơn giản và táo bạo. Một món đồ thiết yếu hằng ngày, đôi giày này dành cho tất cả mọi người. Thân giày bằng da được tô điểm bằng các chi tiết da lộn và lót bên trong tạo sự thoải mái. Đế ngoài bằng cao su tạo độ bám để bạn có thể di chuyển một cách tự tin. Với thiết kế cổ thấp, những đôi giày này là một biểu hiện chân thực nhưng vẫn mang hơi thở đương đại của di sản thể thao adidas.',2600000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779209030056_Adidas%20Forum%20Low.png?alt=media&token=bea4d151-ca2d-4529-9c10-cd0ad0f85644','Sneaker',3,'Adidas','2025-07-06 07:37:12','2026-05-25 13:47:50','',10,'','Unisex','','',1,2600000.00,0),(15,'Adidas Run 80S','Giày adidas cổ điển có đế ngoài bằng cao su có độ bám tốt.\nBuộc dây giày theo phong cách cổ điển với sự thoải mái theo phong cách mới. Đôi giày lấy cảm hứng từ chạy bộ này có đệm đế giữa Cloudfoam sang trọng để bạn không bị mỏi khi đi bộ cả ngày dài. Lớp phủ da lộn và 3 sọc dễ nhận biết tạo nên dấu hiệu phong cách.',3400000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779209045583_Adidas%20Run%2080S.png?alt=media&token=c1f26912-3fe0-4872-897c-29e233404c92','Thể thao',4,'Adidas','2025-07-06 08:44:25','2026-05-25 13:47:50','Nâu/Xám',15,'','Unisex','','',1,3400000.00,0),(16,'Adidas Tokyo','Đôi giày dáng thấp thanh thoát lấy cảm hứng từ phong cách giày chạy bộ thập niên 70.\nLên đồ theo phong cách retro với đôi giày adidas Tokyo. Thân giày bằng da lộn mềm mại với dáng thấp, ôm chân vừa vintage lại vừa hiện đại. Bên dưới là đế ngoài bằng cao su bám tốt để bạn tự tin sải bước. Hãy mang đôi giày trainer này và di chuyển từ chỗ làm đến cuối tuần và mọi dịp casual khác.',2400000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779209054464_Adidas%20Tokyo.jpg?alt=media&token=d3cb8a08-4a7e-4924-96d8-aba5d94531b8','Thể thao',4,'Adidas','2025-07-06 08:48:48','2026-05-25 13:47:50','',10,'','Unisex','','',1,2400000.00,0),(17,'Adidas Adistar CS','NGHỆ THUẬT CHẠY BỘ TỐC ĐỘ CHẬM VÀ CỰ LY DÀI.\nLấy cảm hứng từ khái niệm chuyển động vĩnh cửu, giày ADISTAR CS hỗ trợ các runner chinh phục kỷ lục cá nhân cự ly dài và hơn thế nữa. Nhằm mang lại cảm giác thoải mái và nâng đỡ, đôi giày này có đường cong dài ôm dọc mũi giày, tạo chuyển động nhịp nhàng, mượt mà và đều đặn giúp bạn sải bước tiếp theo. Đế giữa REPETITOR và REPETITOR+ mật độ kép kết hợp giữa mút foam siêu nhẹ tạo lớp đệm đàn hồi và hợp chất cứng cáp bao bọc gót giày.',2520000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779209068823_Adidas%20Adistar%20CS.png?alt=media&token=84dac11e-d2e3-4beb-8c8c-76fd40362147','Thể thao',4,'Adidas','2025-07-06 08:51:31','2026-05-25 13:47:50','',16,'','Unisex','','',1,2520000.00,0),(18,'Adidas Samba OG','SAMBA ORIGINALS\nRa đời trên sân bóng, giày Samba là biểu tượng kinh điển của phong cách đường phố. Phiên bản này trung thành với di sản, thể hiện qua thân giày bằng da mềm, dáng thấp, nhã nhặn, các chi tiết phủ ngoài bằng da lộn và đế gum, biến đôi giày trở thành item không thể thiếu trong tủ đồ của tất cả mọi người - cả trong và ngoài sân cỏ.',2700000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779209080920_Adidas%20Samba%20OG.png?alt=media&token=fb97fab1-4802-4cbe-9e4e-dbd9f99ced3d','Chạy bộ',1,'Adidas','2025-07-06 08:55:41','2026-05-25 13:47:50','',9,'','Unisex','','',1,2700000.00,0),(19,'Biti\'s Đen ','BVM002777DEN là lựa chọn lý tưởng dành cho quý ông yêu thích phong cách cổ điển pha chút hiện đại, với tông màu nâu socola trầm ấm, dễ phối và sang trọng. Mẫu giày tây buộc dây này giúp hoàn thiện vẻ ngoài lịch thiệp cho các dịp đi làm, hội họp hay sự kiện trang trọng.\n\nĐược làm từ chất liệu da tổng hợp cao cấp, bề mặt bóng mịn dễ lau chùi và có độ bền vượt trội. Mũi giày bo tròn nhẹ, form giày gọn gàng giúp tôn dáng bàn chân mà vẫn tạo cảm giác thoải mái trong từng bước đi. Các đường may tỉ mỉ chạy dọc thân giày không chỉ tăng độ bền mà còn mang lại nét tinh tế cho thiết kế tổng thể.',845000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779420643160_Biti\'s%20%C4%90en%20BVM002777DEN.jpg?alt=media&token=84e94852-a9a7-47a1-8243-064084bfbf98','Công sở',2,'Bitis','2025-07-06 09:02:13','2026-05-25 13:47:50','',5,'','Nam','','',1,845000.00,0),(20,'Biti\'s Mocasin','Giày Mọi Nam BMM002077DEN đến từ thương hiệu Biti’s là lựa chọn hoàn hảo dành cho quý ông yêu thích phong cách tối giản, tinh tế và tiện dụng. Sản phẩm nổi bật với màu đen sang trọng, dễ dàng phối hợp cùng nhiều trang phục từ công sở đến thường ngày.\n\nĐược chế tác từ da tổng hợp cao cấp với vân giả da tự nhiên, đôi giày không chỉ mang đến vẻ ngoài lịch lãm mà còn dễ dàng vệ sinh và giữ form tốt trong thời gian dài. Thiết kế slip-on không dây hỗ trợ thao tác mang vào – tháo ra nhanh chóng, tiết kiệm thời gian mà vẫn đảm bảo vừa vặn nhờ vào phần thun ôm gót chân linh hoạt.',820000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779420729830_Biti\'s%20Mocasin.jpg?alt=media&token=f499680f-3ee4-46d3-bac8-31e5292a78fb','Công sở',2,'Bitis','2025-07-06 09:05:06','2026-05-25 13:47:50','',16,'','Unisex','','',1,820000.00,0),(21,'Biti\'s Mocasin Nâu','Giày Mọi Nam BMM002077NAD đến từ thương hiệu Biti’s là sự kết hợp hoàn hảo giữa phong cách cổ điển và hiện đại, tạo nên vẻ ngoài lịch lãm, sang trọng nhưng vẫn cực kỳ thoải mái khi sử dụng. Với tông nâu trầm nam tính, sản phẩm là lựa chọn lý tưởng cho những buổi gặp mặt, đi làm hoặc dạo phố.\n\nThiết kế giày mọi không dây giúp người dùng dễ dàng xỏ chân và tháo ra nhanh chóng. Chất liệu da tổng hợp cao cấp có độ bóng nhẹ, vân giả da cá tính, không chỉ mang lại vẻ ngoài sang trọng mà còn dễ dàng vệ sinh. Phần lót giày êm ái cùng đế cao su đàn hồi giúp giảm áp lực khi di chuyển, mang lại sự thoải mái tối đa ngay cả khi sử dụng trong thời gian dài.\n\nDành cho những quý ông yêu thích sự chỉn chu, tinh tế nhưng vẫn mong muốn cảm giác nhẹ nhàng và linh hoạt trong từng bước chân, mẫu giày BMM002077NAD chính là người bạn đồng hành lý tưởng trong mọi hoàn cảnh.',800000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779420790648_Biti\'s%20Mocasin%20N%C3%A2u.jpg?alt=media&token=0eb41725-75f5-47ef-90a9-90bb3430f72c','Công sở',2,'Bitis','2025-07-06 12:43:36','2026-05-25 13:47:50','',12,'','Unisex','','',1,800000.00,0),(22,'Biti\'s BVM002077','Điểm nổi bật của mẫu giày nằm ở chất liệu da tổng hợp bóng mịn, giúp tăng tính thẩm mỹ và dễ dàng vệ sinh. Form giày ôm gọn bàn chân, kết hợp với đường may thủ công sắc sảo tại mũi giày và thân trên, tạo nên tổng thể chỉn chu, mạnh mẽ. Phần mũi giày bo tròn nhẹ giúp tạo cảm giác cân đối khi di chuyển và phù hợp với nhiều dáng chân khác nhau.\n\nLót giày mềm mại, thoáng khí tốt, hạn chế hầm nóng trong thời gian dài sử dụng. Đặc biệt, đế cao su tổng hợp chống trơn trượt, độ ma sát cao và bám sàn tốt, nâng cao sự an toàn khi di chuyển. Mặt đế được thiết kế tỉ mỉ với các rãnh chống trượt giúp tạo sự chắc chắn từng bước chân.\n\nSản phẩm là lựa chọn không thể thiếu dành cho các quý ông yêu thích sự chỉn chu, tinh tế và tiện dụng trong cùng một đôi giày. BVM002477DEN mang lại trải nghiệm thoải mái, tôn dáng và khẳng định phong cách chuyên nghiệp.',950000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779420862690_Biti\'s%20BVM002077.jpg?alt=media&token=5572be10-2f92-4077-885a-52bf4b1253a7','Công sở',2,'Bitis','2025-07-06 12:45:56','2026-05-25 13:47:50','',6,'','Unisex','','',1,950000.00,0),(23,'Nike Air Max TL 2.5','Làm sống lại một đôi giày được ưa chuộng vào cuối những năm 90, Air Max TL 2.5 mang lại vẻ đẹp Air Max gợn sóng mang tính biểu tượng. Vải dệt thoáng khí kết hợp với da tổng hợp mịn màng để tạo nên lớp hoàn thiện sạch sẽ, trong khi đệm Max Air toàn chiều dài mang lại cảm giác nảy cho từng bước chân.\r\n\r\n\r\nMàu sắc hiển thị: Trắng/Đen/Xám khói/Nho dại\r\nKiểu dáng: FZ4110-105\r\nQuốc gia/Khu vực xuất xứ: Việt Nam',5279000.00,'nikeair1.avif','Thể thao',4,'Nike','2025-07-06 12:51:21','2026-05-25 13:47:50','',13,'','Unisex','','',1,5279000.00,0),(24,'Nike Initiator','Hãy đi nhiều dặm với sự hỗ trợ thoải mái của Nike Initiator. Nó kết hợp phần trên thoáng khí với đệm mềm mại giúp bạn tự tin sải bước.\r\n\r\n\r\nMàu sắc hiển thị: Trắng/Trắng/Đen/Bạc kim loại\r\nKiểu dáng: FQ6873-101\r\nQuốc gia/Khu vực xuất xứ: Indonesia, Việt Nam',2499000.00,'nikea1.avif','Thể thao',4,'Nike','2025-07-06 12:53:41','2026-05-25 13:47:50','',5,'','Unisex','','',1,2499000.00,0),(25,'Nike Pegasus ','Pegasus Premium tăng cường đệm phản ứng với bộ ba công nghệ chạy mạnh mẽ nhất của chúng tôi: bọt ZoomX, bộ phận Air Zoom được điêu khắc và bọt ReactX. Đây là đôi Pegasus phản ứng nhanh nhất từ ​​trước đến nay, mang lại khả năng hoàn trả năng lượng cao không giống bất kỳ đôi nào khác. Với phần trên nhẹ hơn không khí, nó giúp giảm trọng lượng và tăng khả năng thoáng khí để bạn có thể bay nhanh hơn.\n\n\nMàu sắc hiển thị: Xanh lá cây nhạt/Xốp bạc hà/Bạc kim loại/Đỏ thẫm sáng\nKiểu dáng: HQ2592-301\nQuốc gia/Khu vực xuất xứ: Việt Nam',6179000.00,'https://firebasestorage.googleapis.com/v0/b/appbangiay-dac0f.firebasestorage.app/o/products%2F1779420456365_Pegasus%20Premium.avif?alt=media&token=85cdccb8-61e4-44c6-8433-a20655d4b0d4','Thể thao',4,'Nike','2025-07-06 12:56:29','2026-05-25 13:47:50','',10,'','Unisex','','',1,6179000.00,0),(30,'Nike Dunk Hight 123','',1223.00,'','',NULL,'','2026-05-25 14:00:14','2026-05-25 14:26:19',NULL,0,'','Unisex','','',1,1223.00,0);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review_likes`
--

DROP TABLE IF EXISTS `review_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review_likes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `review_id` int NOT NULL,
  `firebase_uid` varchar(128) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_review_like` (`review_id`,`firebase_uid`),
  CONSTRAINT `review_likes_ibfk_1` FOREIGN KEY (`review_id`) REFERENCES `product_reviews` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review_likes`
--

LOCK TABLES `review_likes` WRITE;
/*!40000 ALTER TABLE `review_likes` DISABLE KEYS */;
INSERT INTO `review_likes` VALUES (3,1,'1Bd9l6OnjSaQhgk1qrLw1EdvLwU2','2026-05-28 01:46:30');
/*!40000 ALTER TABLE `review_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `full_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Công Hoàng','$2y$10$8dGzQhkoVbW6N4lJsAPUHuzQnaJ/LGJfCeEucQmWUKoktxJbFyYwC','conghoang@gmail.com','Công Hoàng','Quận 12','0337116123','2025-06-29 11:01:58','2025-07-06 16:13:41',1),(2,'hoang','$2y$10$2.4Jyt5wHY0SeUFjJyZeROxzHP2mIJul45vZ1XSNTR6uUO.QmhZW6','hoang@gmail.com','Công Hoàng','','','2025-07-04 09:06:46','2025-07-04 09:06:46',1),(5,'hoang2','$2y$10$TYpJBHX86WgC9imKXZSLO.FVltHMO5ZejS5BwC1topo5qUHPTkSk2','hoang2@gmail.com','hoang2','','','2025-07-05 15:23:54','2025-07-05 15:23:54',1),(6,'khai','$2y$10$.YsCsXvDo2t2gurugnUT4eqANX2te0JezrOHtarz6XwlpSqKCgVy6','khai@gmail.com','Minh Khai','Phường 13, Tân Bình, Hồ Chí Minh','0337123456','2025-07-06 13:02:48','2025-07-06 13:19:42',1),(7,'thanhtam','$2b$12$C36Yn7ATpsBD6tkU6D.QOeYK0Q/DfzdiYZZIShjLBy71pQsvGScIO','thanhta@gmail.com','Hoàng Thanh Tâm Tâm','Hà Nội','044412345678','2025-07-06 16:10:54','2026-05-18 14:10:53',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-29 17:26:12
