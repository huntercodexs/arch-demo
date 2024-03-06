-- MySQL dump 10.13  Distrib 8.0.28, for Linux (x86_64)
--
-- Host: localhost    Database: huntercodexs
-- ------------------------------------------------------
-- Server version	8.0.28-0ubuntu0.20.04.3

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
-- Table structure for table `client_authorizator`
--

LOCK TABLES `client_authorizator` WRITE;
DROP TABLE IF EXISTS `client_authorizator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client_authorizator` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `client` varchar(255) DEFAULT NULL,
  `secret` varchar(255) DEFAULT NULL,
  `scope` varchar(255) DEFAULT NULL,
  `accessTokenValiditySeconds` int DEFAULT NULL,
  `refreshTokenValiditySeconds` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_authorizator`
--

LOCK TABLES `client_authorizator` WRITE;
/*!40000 ALTER TABLE `client_authorizator` DISABLE KEYS */;
INSERT INTO `client_authorizator` VALUES
(1,'arch_demo_client_1','MTExMTExMTEtMjIyMi0zMzMzLTQ0NDQtNTU1NTU1NTU1NTU1','read-write',3600,7200),
(2,'arch_demo_client_2','MTExMTExMTEtMjIyMi0zMzMzLTQ0NDQtNjY2NjY2NjY2NjY2','read',3600,7200);
/*!40000 ALTER TABLE `client_authorizator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operator_authorizator`
--

DROP TABLE IF EXISTS `operator_authorizator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operator_authorizator` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `deleted` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operator_authorizator`
--

LOCK TABLES `operator_authorizator` WRITE;
/*!40000 ALTER TABLE `operator_authorizator` DISABLE KEYS */;
INSERT INTO `operator_authorizator` VALUES
(1,'OAUTH2DEMO_ADMIN','1234567890','ROLE_ADMIN',null,0,1),
(2,'OAUTH2DEMO_USER','1234567890','ROLE_USER',null,0,1),
(3,'OAUTH2DEMO_CLIENT','1234567890','ROLE_CLIENT',null,0,1);
/*!40000 ALTER TABLE `operator_authorizator` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-18 14:14:47
