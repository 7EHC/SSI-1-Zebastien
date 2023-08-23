use mydb;
use integrated;
drop table users;
truncate table users;
create table users (
	id int auto_increment primary key,
    username varchar(45) unique,
    name varchar(100) unique,
    email varchar(150) unique,
    role enum('admin', 'announcer') not null default 'announcer',
    createdOn timestamp not null default current_timestamp,
    updatedOn timestamp not null default current_timestamp on update current_timestamp
);

