CREATE TABLE ucm_friends_cover (
  uri varchar2(100) default '' NOT NULL ,
  userId varchar2(100) default NULL  ,
  fileId varchar2(100) default NULL  ,
  extend1 varchar2(100) default NULL ,
  extend2 varchar2(100) default NULL ,
  fkdomain varchar2(200) default NULL,
  version int default 0 ,
  flags int default 0 ,
  PRIMARY KEY  (uri)
);

CREATE TABLE ucm_friends_resource (
  uri varchar2(100) default '' NOT NULL ,
  shareUri varchar2(100) default NULL  ,
  fileId varchar2(100) default NULL  ,
  fileName varchar2(100) default NULL  ,
  fileSize varchar2(50) default NULL  ,
  fileExt varchar2(30) default NULL  ,
  fileType varchar2(10) default NULL  ,
  createtime varchar2(50) default NULL  ,
  modifytime varchar2(50) default NULL  ,
  location varchar2(100) default NULL  ,
  extend1 varchar2(100) default NULL ,
  extend2 varchar2(100) default NULL ,
  fkdomain varchar2(200) default NULL,
  version int default 0 ,
  flags int default 0 ,
  PRIMARY KEY  (uri)
);

CREATE TABLE ucm_friends_share (
  uri varchar2(100) default '' NOT NULL ,
  userId varchar2(100) default NULL  ,
  content varchar2(1000) default NULL  ,
  createtime varchar2(50) default NULL  ,
  position varchar2(100) default NULL  ,
  extend1 varchar2(100) default NULL ,
  extend2 varchar2(100) default NULL ,
  fkdomain varchar2(200) default NULL,
  version int default 0 ,
  flags int default 0 ,
  PRIMARY KEY  (uri)
);

CREATE TABLE ucm_friends_comment (
  uri varchar2(100) default '' NOT NULL ,
  shareUri varchar2(100) default NULL  ,
  fromUserId varchar2(100) default NULL  ,
  createtime varchar2(50) default NULL  ,
  content varchar2(500) default NULL  ,
  toUserId varchar2(100) default NULL  ,
  commentType varchar2(10) default NULL  ,
  extend1 varchar2(100) default NULL ,
  extend2 varchar2(100) default NULL ,
  fkdomain varchar2(200) default NULL,
  version int default 0 ,
  flags int default 0 ,
  PRIMARY KEY  (uri)
);


create table ucmbfriends_t (
	uri varchar2(200),
	name varchar2(100)
);

insert into jiveversion(name,version)values('ucmbfriends',1);
