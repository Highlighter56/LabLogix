# Database Instructions for first-time users
There are several MySQL Files created for the database 

## Run the MySQL Files in this order:
1. [lablogix.sql ](lablogix.sql)
    
    This is the initial file for the database. Run the file to create the database, create tables, and reference the proper foreign keys

2. [sessionUpdates](sessionUpdates.sql)

    Automatically adding current timestamps for each session login times 

3. [notificationTable](notificationTable.sql)

    creates a notification table, and inserts blank/empty data

## Additional Files and their purposes

[testFile](testFile.sql): shows all of the data in each column of the table (can be adjusted to show column headers as well)

[testData](testData.sql): test data created for 

[clearTables](clearTables.sql): clears test data from each table (so that when new data is added to *testData*, there's no conflicts)

[deviceAvailabilityQueries](deviceAvailabilityQueries.sql): queries listing the device status' across 2 rooms