use labLogix;

-- creating notification table
create table notification( notificationID int primary key, status boolean);

-- inserting test data
insert into notification(notificationID, status) values
	(1, false),
    (2, false),
    (3, false),
    (4, false);