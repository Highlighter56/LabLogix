-- show databases;
-- run this file first
create database labLogix;
use labLogix;
show tables;
show databases;

create table if not exists users(
userID int auto_increment primary key, 
name varchar(100), 
email varchar(100), 
password varchar(300), 
userType enum('student', 'faculty', 'administrator'));
describe users;

create table if not exists room260(
room260ID int auto_increment primary key, 
sessionlogged DateTIME default current_timestamp, 
userID int, 
status enum('open', 'closed', 'full'), 
capacity int, 
currentOccupancy int);

create table if not exists room259 (
room259ID int auto_increment primary key, 
sessionlogged DateTIME default current_timestamp, 
userID int, 
status enum('open', 'closed', 'full'), 
capacity int, 
currentOccupancy int);

create table if not exists pc(
pcID int auto_increment primary key, 
status enum('available', 'in_use', 'offline'), 
room260ID int NULL, 
room259ID int NULL);

create table if not exists printer(
printerID int auto_increment primary key, 
status enum('available', 'in_use', 'offline'), 
room259ID int not NULL);

create table if not exists printer3d(
printer3dID int auto_increment primary key, 
status enum('available', 'in_use', 'offline'), 
room260ID int not NULL);

show tables;

-- foreign keys run down here
alter table room260
	add constraint fk_room260_user 
    foreign key (userID) references users(userID);
    describe room260;

alter table room259 
	add constraint fk_room259_user 
    foreign key (userID) references users(userID);
	describe room259;
    
alter table pc
	add constraint fk_pc_room259
    foreign key (room259ID) references room259(room259ID),
    add constraint fk_pc_room260
    foreign key (room260ID) references room260(room260ID),
    add constraint check_pc_roomNum
		check(
			(room260ID is not NULL and room259ID is NULL) or
			(room260ID is NULL and room259ID is not NULL)
		);

alter table printer
	add constraint fk_printer_room259
    foreign key (room259ID) references room259(room259ID);
    describe printer;

alter table printer3d
	add constraint fk_printer3d_room260 
    foreign key (room260ID) references room260(room260ID);
    describe printer3d;

show tables;