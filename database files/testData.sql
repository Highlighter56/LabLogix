use labLogix;

-- users table
insert into users (name, email, password, userType) values
	('Alice Johnson', 'johnsona1@testemail.com', 'Alicespassword123', 'student'), 
	('Bob Lee', 'leeb72@testemail.com', 'Bobspassword456', 'student'), 
	('Charlie Kim', 'kimc12@testemail.com', 'Charliespassword789', 'faculty'), 
	('Diana Ross', 'rossd@testemail.com', 'Dianaspassword1011', 'administrator'), 
	('Ethan Chen', 'chene8@testemail.com', 'Ethanspassword1213', 'student'),
    ('Evelyn Baron', 'barone54@testemail.com', 'Evelynspassword1415', 'faculty');

insert into room260 (sessionlogin, sessionlogout, userID, status, capacity, currentOccupancy) values
	(NOW(), NULL, 
		(select userID from users where email = 'johnsona1@testemail.com'),
		'open', 30, 12), -- we can edit this line eventually to have it pull from the website instead of beind hard coded, but this is temporary
	(NOW(), NULL, 
		(select userID from users where email = 'leeb72@testemail.com'),
		'full', 30, 30), 
	(NOW(), NOW(), (select userID from users where email = 'kimc12@testemail.com'),
    'closed', 30, 0);

insert into room259 (sessionlogin, sessionlogout, userID, status, capacity, currentOccupancy) values
(NOW(), NULL, 
	(select userID from users where email = 'rossd@testemail.com'),
	'open', 25, 10), 
(NOW(), NULL,
	(select userID from users where email = 'chene8@testemail.com'),
    'full', 25, 25), 
(NOW(), NOW(), 
	(select userID from users where email = 'barone54@testemail.com'),
    'closed', 25, 0);

insert into pc (status, room260ID, room259ID) values
	-- room260
    ('available', 1, NULL), 
	('in_use', 1, NULL), 
	('offline', 1, NULL), 
	-- room259
	('available', NULL, 2), 
	('in_use', NULL, 2), 
	('offline', NULL, 2);

insert into printer (status, room259ID) values
('available', 1), 
('in_use', 2), 
('offline', 2);

insert into printer3d (status, room260ID) values
('available', 1), 
('in_use', 2), 
('offline', 2);

-- allow updates for testing
set SQL_SAFE_UPDATES = 0;

-- log out of active sessions (run for the test data)
update room260
	set sessionlogout = NOW(),
    currentOccupancy = greatest(currentOccupancy - 1, 0)
    where sessionlogout is NULL and room260ID is not NULL;
    
update room259
	set sessionlogout = NOW(),
    currentOccupancy = greatest(currentOccupancy - 1, 0)
    where sessionlogout is NULL and room259ID is not NULL;


/* session logout for real data/users
set @userID = (select userID from users where email = 'insertemailhere');

update room260
	set sessionlogout = NOW(), 
    currentOccupancy = greatest(currentOccupancy - 1,0)
    where userID = @userID and session logout is NULL;
*/

-- print test data
select * from users;
select * from room260;
select * from room259;
select * from pc;
select * from printer;
select * from printer3d;