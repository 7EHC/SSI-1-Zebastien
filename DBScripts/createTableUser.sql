use mydb;

create table users (
	id int auto_increment primary key,
    username varchar(45) unique,
    name varchar(100) unique,
    email varchar(150) unique,
    role enum('admin', 'announcer'),
    createdOn datetime,
    updatedOn datetime
);

