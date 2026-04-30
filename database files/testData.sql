use labLogix;

insert into users (name, email, password, userType) values
	('Alice Johnson', 'johnsona1@testemail.com', 'pass123', 'student'),
	('Bob Lee', 'leeb72@testemail.com', 'pass123', 'student'),
	('Charlie Kim', 'kimc12@testemail.com', 'pass123', 'faculty'),
	('Diana Ross', 'rossd@testemail.com', 'pass123', 'administrator'),
	('Ethan Chen', 'chene8@testemail.com', 'pass123', 'student'),
	('Fiona Patel', 'patelf@testemail.com', 'pass123', 'student'),
	('George White', 'whiteg@testemail.com', 'pass123', 'faculty'),
	('Hannah Scott', 'scotth@testemail.com', 'pass123', 'student'),
	('Isaac Green', 'greeni@testemail.com', 'pass123', 'student'),
	('Julia Adams', 'adamsj@testemail.com', 'pass123', 'administrator'),
	('Kevin Park', 'parkk@testemail.com', 'pass123', 'student'),
	('Lily Nguyen', 'nguyenl@testemail.com', 'pass123', 'student'),
	('Marcus Brown', 'brownm@testemail.com', 'pass123', 'faculty'),
	('Nina Lopez', 'lopezn@testemail.com', 'pass123', 'student'),
	('Owen Clark', 'clarko@testemail.com', 'pass123', 'student'),
	('Priya Shah', 'shahp@testemail.com', 'pass123', 'faculty'),
	('Quinn Davis', 'davisq@testemail.com', 'pass123', 'student'),
	('Ryan Moore', 'moorer@testemail.com', 'pass123', 'student'),
	('Sofia Torres', 'torress@testemail.com', 'pass123', 'student'),
	('Tyler Brooks', 'brookst@testemail.com', 'pass123', 'administrator'),
	('Uma Patel', 'patelu@testemail.com', 'pass123', 'student'),
	('Victor Cruz', 'cruzv@testemail.com', 'pass123', 'student'),
	('Wendy Zhang', 'zhangw@testemail.com', 'pass123', 'faculty'),
	('Xavier Reed', 'reedx@testemail.com', 'pass123', 'student'),
	('Yara Ali', 'aliy@testemail.com', 'pass123', 'student'),
	('Zane Scott', 'scottz@testemail.com', 'pass123', 'student');

insert into room260 (sessionlogin, sessionlogout, userID, status, capacity, currentOccupancy) values
	(NOW(), NULL, (select userID from users where email='johnsona1@testemail.com'), 'open', 15, 6),
	(NOW(), NULL, (select userID from users where email='kimc12@testemail.com'), 'open', 15, 10),
	(NOW(), NULL, (select userID from users where email='whiteg@testemail.com'), 'full', 15, 15),
	(NOW(), NOW(), (select userID from users where email='rossd@testemail.com'), 'closed', 15, 0),
	(NOW(), NULL, (select userID from users where email='patelf@testemail.com'), 'open', 15, 3),
	(NOW(), NULL, (select userID from users where email='parkk@testemail.com'), 'open', 15, 5),
	(NOW(), NULL, (select userID from users where email='brownm@testemail.com'), 'open', 15, 9),
	(NOW(), NULL, (select userID from users where email='shahp@testemail.com'), 'full', 15, 15),
	(NOW(), NOW(), (select userID from users where email='brookst@testemail.com'), 'closed', 15, 0),
	(NOW(), NULL, (select userID from users where email='zhangw@testemail.com'), 'open', 15, 7);

insert into room259 (sessionlogin, sessionlogout, userID, status, capacity, currentOccupancy) values
	(NOW(), NULL, (select userID from users where email='leeb72@testemail.com'), 'open', 25, 12),
	(NOW(), NULL, (select userID from users where email='chene8@testemail.com'), 'full', 25, 25),
	(NOW(), NULL, (select userID from users where email='scotth@testemail.com'), 'open', 25, 18),
	(NOW(), NOW(), (select userID from users where email='adamsj@testemail.com'), 'closed', 25, 0),
	(NOW(), NULL, (select userID from users where email='greeni@testemail.com'), 'open', 25, 7),
	(NOW(), NULL, (select userID from users where email='nguyenl@testemail.com'), 'open', 25, 14),
	(NOW(), NULL, (select userID from users where email='lopezn@testemail.com'), 'full', 25, 25),
	(NOW(), NULL, (select userID from users where email='clarko@testemail.com'), 'open', 25, 11),
	(NOW(), NOW(), (select userID from users where email='davisq@testemail.com'), 'closed', 25, 0),
	(NOW(), NULL, (select userID from users where email='moorer@testemail.com'), 'open', 25, 8);

insert into pc (status, room260ID, room259ID, userID) values
	('available', 1, NULL, (select userID from users where email='johnsona1@testemail.com')),
	('in_use', 1, NULL, (select userID from users where email='leeb72@testemail.com')),
	('offline', 2, NULL, (select userID from users where email='kimc12@testemail.com')),
	('available', 3, NULL, (select userID from users where email='whiteg@testemail.com')),
	('in_use', 3, NULL, (select userID from users where email='patelf@testemail.com')),
	('available', 6, NULL, (select userID from users where email='parkk@testemail.com')),
	('in_use', 7, NULL, (select userID from users where email='brownm@testemail.com')),
	('offline', 8, NULL, (select userID from users where email='shahp@testemail.com')),
	('available', NULL, 1, (select userID from users where email='scotth@testemail.com')),
	('in_use', NULL, 2, (select userID from users where email='rossd@testemail.com')),
	('offline', NULL, 2, (select userID from users where email='chene8@testemail.com')),
	('available', NULL, 3, (select userID from users where email='greeni@testemail.com')),
	('in_use', NULL, 5, (select userID from users where email='adamsj@testemail.com')),
	('available', NULL, 6, (select userID from users where email='nguyenl@testemail.com')),
	('in_use', NULL, 7, (select userID from users where email='lopezn@testemail.com')),
	('offline', NULL, 8, (select userID from users where email='clarko@testemail.com'));

insert into printer (status, room259ID, userID) values
	('offline', 8, (select userID from users where email='clarko@testemail.com'));

insert into printer3d (status, room260ID, userID) values
	('available', 1, (select userID from users where email='kimc12@testemail.com')),
	('in_use', 2, (select userID from users where email='rossd@testemail.com')),
	('offline', 3, (select userID from users where email='whiteg@testemail.com')),
	('available', 4, (select userID from users where email='patelf@testemail.com'));

insert into notification (notificationID, status) values
(1, false),
(2, false),
(3, false),
(4, false);
    
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