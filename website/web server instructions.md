# Instructions for Running the server

If it doesn't work, close VS code and reopen

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