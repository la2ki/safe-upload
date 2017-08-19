/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     4/19/2017 10:28:00 AM                        */
/*==============================================================*/


drop table if exists CUSTOMPROPERTY;

drop table if exists FILE;

drop table if exists FILECUSTOMPROPERTY;

drop table if exists FOLDER;

drop table if exists ROLE;

drop table if exists SHARED;

drop table if exists USER;

/*==============================================================*/
/* Table: CUSTOMPROPERTY                                        */
/*==============================================================*/
create table CUSTOMPROPERTY
(
   CUSTOMPROPERTYID     bigint not null auto_increment,
   NAME                 varchar(64) not null,
   VALUE                varchar(128) not null,
   VALUETYPE            varchar(64) not null,
   primary key (CUSTOMPROPERTYID)
);

/*==============================================================*/
/* Table: FILE                                                  */
/*==============================================================*/
create table FILE
(
   FILEID               varchar(36) not null,
   USERID               varchar(36),
   FOLDERID             varchar(36),
   NAME                 varchar(64) not null,
   PATH                 text not null,
   TYPE                 varchar(128),
   SIZE                 decimal,
   CREATEDON            timestamp not null,
   DESCRIPTION          varchar(4096),
   primary key (FILEID)
);

/*==============================================================*/
/* Table: FILECUSTOMPROPERTY                                    */
/*==============================================================*/
create table FILECUSTOMPROPERTY
(
   FILEID               varchar(36) not null,
   CUSTOMPROPERTYID     bigint not null,
   primary key (FILEID, CUSTOMPROPERTYID)
);

/*==============================================================*/
/* Table: FOLDER                                                */
/*==============================================================*/
create table FOLDER
(
   FOLDERID             varchar(36) not null,
   USERID               varchar(36),
   FOL_FOLDERID         varchar(36),
   NAME                 varchar(64) not null,
   PATH                 text not null,
   CREATEDON            timestamp not null,
   DESCRIPTION          varchar(4096),
   primary key (FOLDERID)
);

/*==============================================================*/
/* Table: ROLE                                                  */
/*==============================================================*/
create table ROLE
(
   ROLEID               int not null auto_increment,
   NAME                 varchar(64) not null,
   ISDEPRICATED         bool not null,
   primary key (ROLEID)
);

/*==============================================================*/
/* Table: SHARED                                                */
/*==============================================================*/
create table SHARED
(
   USERID               varchar(36) not null,
   FOLDERID             varchar(36) not null,
   READPERMISSION               bool,
   WRITEPERMISSION              bool,
   primary key (USERID, FOLDERID)
);

/*==============================================================*/
/* Table: USER                                                  */
/*==============================================================*/
create table USER
(
   USERID               varchar(36) not null,
   ROLEID               int,
   EMAIL                varchar(256) not null,
   PASSWORD             varchar(1024) not null,
   REGISTREDON          timestamp not null,
   LASTLOGIN            timestamp,
   DISABLED             bool not null,
   MAXSPACE             decimal,
   SPACEOCCUPIED        decimal,
   primary key (USERID)
);

alter table FILE add constraint FK_RELATIONSHIP_6 foreign key (USERID)
      references USER (USERID) on delete restrict on update restrict;

alter table FILE add constraint FK_RELATIONSHIP_7 foreign key (FOLDERID)
      references FOLDER (FOLDERID) on delete restrict on update restrict;

alter table FILECUSTOMPROPERTY add constraint FK_RELATIONSHIP_4 foreign key (FILEID)
      references FILE (FILEID) on delete restrict on update restrict;

alter table FILECUSTOMPROPERTY add constraint FK_RELATIONSHIP_5 foreign key (CUSTOMPROPERTYID)
      references CUSTOMPROPERTY (CUSTOMPROPERTYID) on delete restrict on update restrict;

alter table FOLDER add constraint FK_RELATIONSHIP_2 foreign key (USERID)
      references USER (USERID) on delete restrict on update restrict;

alter table FOLDER add constraint FK_RELATIONSHIP_3 foreign key (FOL_FOLDERID)
      references FOLDER (FOLDERID) on delete restrict on update restrict;

alter table SHARED add constraint FK_RELATIONSHIP_8 foreign key (USERID)
      references USER (USERID) on delete restrict on update restrict;

alter table SHARED add constraint FK_RELATIONSHIP_9 foreign key (FOLDERID)
      references FOLDER (FOLDERID) on delete restrict on update restrict;

alter table USER add constraint FK_RELATIONSHIP_1 foreign key (ROLEID)
      references ROLE (ROLEID) on delete restrict on update restrict;

insert into ROLE values (1, 'ADMIN', 0);
insert into ROLE values (2, 'USER', 0);