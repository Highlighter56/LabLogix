# Instructions for Running the server

You will need to create .env 
If it doesn't work, close VS code and reopen.

## 0. Create a .env file
In the website folder, create a ".env" file. In the file, paste this. This file will not be copied over to github, so your password will not be shared

    DB_HOST = localhost
    DB_USER = root
    DB_PASSWORD = PASSWORD (substitute PASSWORD for your MySQL PAssword)
    DB_NAME = labLogix

## 1. navigate into the project folder (Lablogix, but if you're running it from VS code terminal, this step is unecessary)

    cd into project folder

## 2. navigate into the website folder
    cd website

## 3. Initialize a node project
    npm init -y

## 4. Install packages
    npm install express mysql2 cors dotenv

express -> backend framework to create routes
mysql2 -> connecting Node to MySQL database
cors -> allows frontend and backend communication
dotenv -> local environment variables to store things in .env file

## 5. Run the server directly
    node server.js

## 6. Run the server using npm
    npm start