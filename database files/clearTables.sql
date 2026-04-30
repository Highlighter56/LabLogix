set foreign_key_checks = 0;

truncate table users;
truncate table pc;
truncate table printer;
truncate table printer3d;
truncate table room260;
truncate table room259;
truncate table notification;

-- set foreign_keys_checks = 1;

-- print test data
select * from users;
select * from room260;
select * from room259;
select * from pc;
select * from printer;
select * from printer3d;