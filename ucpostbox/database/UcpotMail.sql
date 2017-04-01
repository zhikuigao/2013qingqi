/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.0.51b-community-nt : Database - wuxianji
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`wuxianji` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `wuxianji`;

/*Table structure for table `e_exchange_mail` */

DROP TABLE IF EXISTS `e_exchange_mail`;

CREATE TABLE `e_exchange_mail` (
  `uri` varchar(111) NOT NULL,
  `version` int(122) default NULL,
  `flags` int(50) default NULL,
  `username` varchar(100) default NULL,
  `mailid` varchar(100) default NULL,
  `mailnumber` int(100) default NULL,
  `isnewmail` int(100) default NULL,
  PRIMARY KEY  (`uri`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `e_exchange_mail` */

insert  into `e_exchange_mail`(`uri`,`version`,`flags`,`username`,`mailid`,`mailnumber`,`isnewmail`) values ('-1960',0,0,'test001','-1960',3,0),('-1985',0,0,'test001','-1985',4,0),('-1991',0,0,'test001','-1991',7,0),('-1993',0,0,'test001','-1993',5,0),('-2000',0,0,'test001','-2000',6,0),('-2023',0,0,'test001','-2023',2,0),('-2028',0,0,'test001','-2028',1,0),('-2175',0,0,'test002','-2175',1,0),('-2207',0,0,'test002','-2207',2,0),('-2243',0,0,'test002','-2243',3,0);

/*Table structure for table `e_exchange_savepass` */

DROP TABLE IF EXISTS `e_exchange_savepass`;

CREATE TABLE `e_exchange_savepass` (
  `username` varchar(111) NOT NULL,
  `password` varchar(111) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `e_exchange_savepass` */

insert  into `e_exchange_savepass`(`username`,`password`) values ('test001','abc_123'),('test002','abc_123');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
