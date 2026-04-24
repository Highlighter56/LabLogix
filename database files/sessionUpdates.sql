use labLogix;

-- room260 edits
alter table room260
    change column sessionlogged sessionlogin datetime default current_timestamp,
    add column sessionlogout datetime null after sessionlogin;
    
describe room260;

-- room259
alter table room259
    change column sessionlogged sessionlogin datetime default current_timestamp,
    add column sessionlogout datetime null after sessionlogin;
    
describe room259;