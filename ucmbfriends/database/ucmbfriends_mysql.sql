CREATE TABLE `ucm_friends_cover` (
  `uri` varchar(100) NOT NULL default '' ,
  `userId` varchar(100) default NULL  ,
  `fileId` varchar(100) default NULL  ,
  `extend1` varchar(100) default NULL ,
  `extend2` varchar(100) default NULL ,
  `fkdomain` varchar(200) default NULL,
  `version` int(11) default '0' ,
  `flags` int(11) default NULL ,
  PRIMARY KEY  (uri)
);

CREATE TABLE `ucm_friends_resource` (
  `uri` varchar(100) NOT NULL default '' ,
  `shareUri` varchar(100) default NULL  ,
  `fileId` varchar(100) default NULL  ,
  `fileName` varchar(100) default NULL  ,
  `fileSize` varchar(50) default NULL  ,
  `fileExt` varchar(30) default NULL  ,
  `fileType` varchar(10) default NULL  ,
  `createtime` varchar(50) default NULL  ,
  `modifytime` varchar(50) default NULL  ,
  `location` varchar(100) default NULL  ,
  `extend1` varchar(100) default NULL ,
  `extend2` varchar(100) default NULL ,
  `fkdomain` varchar(200) default NULL,
  `version` int(11) default '0' ,
  `flags` int(11) default NULL ,
  PRIMARY KEY  (uri)
);

CREATE TABLE `ucm_friends_share` (
  `uri` varchar(100) NOT NULL default '' ,
  `userId` varchar(100) default NULL  ,
  `content` varchar(1000) default NULL  ,
  `createtime` varchar(50) default NULL  ,
  `position` varchar(100) default NULL  ,
  `extend1` varchar(100) default NULL ,
  `extend2` varchar(100) default NULL ,
  `fkdomain` varchar(200) default NULL,
  `version` int(11) default '0' ,
  `flags` int(11) default NULL ,
  PRIMARY KEY  (uri)
);

CREATE TABLE `ucm_friends_comment` (
  `uri` varchar(100) NOT NULL default '' ,
  `shareUri` varchar(100) default NULL  ,
  `fromUserId` varchar(100) default NULL  ,
  `createtime` varchar(50) default NULL  ,
  `content` varchar(500) default NULL  ,
  `toUserId` varchar(100) default NULL  ,
  `commentType` varchar(10) default NULL  ,
  `extend1` varchar(100) default NULL ,
  `extend2` varchar(100) default NULL ,
  `fkdomain` varchar(200) default NULL,
  `version` int(11) default '0' ,
  `flags` int(11) default NULL ,
  PRIMARY KEY  (uri)
);


create table ucmbfriends_t (
	uri varchar(200),
	name varchar(100)
);

insert into jiveversion(name,version)values('ucmbfriends',1);