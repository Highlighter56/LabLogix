use labLogix;

alter table pc
	add column userID int,
    add constraint fk_pc
    foreign key (userID) references users(userID);
    
alter table printer
	add column userID int,
    add constraint fk_printer
    foreign key (userID) references users(userID);
    
alter table printer3d
	add column userID int,
    add constraint fk_printer3d
    foreign key (userID) references users(userID);