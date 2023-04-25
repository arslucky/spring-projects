create database customer;

create user if not exists userDev;

alter user userDev identified by 'password';

grant all on customer.* to userDev; 

use customer;

create table if not exists account
(
   id bigint not null auto_increment,
   account_number varchar (255),
   created_at bigint,
   last_modified bigint,
   primary key (id)
)
engine= InnoDB;

alter table account add constraint uc_account unique (account_number);

create table if not exists customer
(
   id bigint not null auto_increment,
   account_id bigint,
   first_name varchar (255),
   last_name varchar (255),
   email varchar (255),
   created_at bigint,
   last_modified bigint,
   primary key (id)
)
engine= InnoDB;

alter table customer add constraint uc_customer unique (first_name, last_name, email);

set @dt = ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000);

insert ignore into account(account_number, created_at, last_modified) values('12345', @dt, @dt);

insert ignore into account(account_number,created_at,last_modified)
values('67890', @dt, @dt);

insert ignore into customer(account_id,first_name,last_name,email,created_at,last_modified)
values((select id from account where account_number= '12345'),'John','Brown','asd@asd.org',@dt,@dt);