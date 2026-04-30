# Instructions for how to run the terminal
Step by step instructions for how to run the terminal

## To do list 

### 1. Open the terminal and go to the repo

Make sure you have a lib folder

    cd Documents/Github/LabLogix

Run "ls" and make sure you see lib, src, scripts, website. If you don't see it, open VS code and create a lib folder on the same level as src, scripts, and website.

Run "ls" again, and make sure you see lib, src, scripts, website.


### 2. Create a .env file (So that you can log into your database)

    touch .env

If nothing shows up, this is normal, and fine. Run this line to confirm the contents

    cat .env 

If it is empty, go to step 3. 

Else, go to Step 4

### 3. Open your VS Code, find the .env file and paste this, but change it to your password 

If you already have this, skip it

    DB_HOST=localhost
    DB_PORT=3306
    DB_USER=root
    DB_PASSWORD="your_mysql_password"
    DB_NAME=lablogix

### 4. Add the MySQL connector, and fix the file name

download the connector, and put the connector in the lib folder. Use this command to check that the connector has the correct name.

[MySQL Downloads Website](https://dev.mysql.com/downloads/connector/j/): Select Platform Independent, you can download either one of the archives.

Download it, unzip it, and drag the connector into the lib folder you created


    ls lib

If what you have is differen't from what is connected, change it to make it match. I ran this because mine was **7** instead of **6**. Only run this line if the connector is named incorrectly

    mv lib/mysql-connector-j-9.7.0.jar lib/mysql-connector-j-9.6.0.jar

    
### 5. Run the PGM
Ensure that you are on the Main.java file, and run 

    ./scripts/run-terminal.sh

If you get an error saying that you don't have permission. Run this line to give yourself permission

    chmod +x scripts/run-terminal.sh

Run this line to build the script and run the terminal

    ./scripts/run-terminal.sh