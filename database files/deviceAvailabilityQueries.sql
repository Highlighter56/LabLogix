-- show the data in each table
use labLogix;
select r.room260ID, r.status, r.capacity, r.currentOccupancy,
    GROUP_CONCAT(device_info separator ', ') AS devices
from room260 r left join ( 
	-- PCs in room260
    select  room260ID, 
		concat('PC ', pcID, ' (', status, ')') as device_info
    from pc where room260ID is not null
	union all
    
    -- 3D printers in room260
    select room260ID, concat('3d printer ', printer3dID, ' (', status, ')')
    from printer3d) as devices on devices.room260ID = r.room260ID
group by r.room260ID;

-- query for room259
select r.room259ID, r.status, r.capacity, r.currentOccupancy,
    GROUP_CONCAT(device_info separator ', ') as devices
from room259 r left join (
    -- PCs in room259
    select room259ID,
        concat('pc ', pcID, ' (', status, ')') as device_info
    from pc
    where room259ID is not null
    union all

    -- printers in room259
    select room259ID,
        concat('printer ', printerID, ' (', status, ')')
    from printer) as devices on devices.room259ID = r.room259ID
group by r.room259ID;