use labLogix;

-- creating notification table
create table notification( notificationID int primary key, status boolean);

-- inserting test data
insert into notification(notificationID, status) values
	-- room capacity reached
	(1, false),
    -- room was full, but no longer is full
    (2, false),
    -- room closes at 8pm
    (3, false),
    -- room opens at 8am
    (4, false);