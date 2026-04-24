require('dotenv').config();
const express = require('express');
const cors = require('cors');
const db = require("./db");

const app = express();

app.use(cors());
app.use(express.json());

app.get('/', (req, res) => {
    res.sendFile(__dirname + '/public/Room260.html');
});

app.get('/room260', (req, res) => {
    res.sendFile(__dirname + '/public/Room260.html');
});

app.get('/room259', (req, res) => {
    res.sendFile(__dirname + '/public/Room259.html');
});

app.get('/usagehistory', (req, res) => {
    res.sendFile(__dirname + '/public/UsageHistory.html');
});

app.get('/style.css', (req, res) => {
    res.sendFile(__dirname + '/public/style.css');
});

app.get('/api/pc', async (req, res) => {
    try{
        const [rows] = await db.query('SELECT * FROM pc');
        res.json(rows);
    } catch (err) {
        console.error(err);
        res.status(500).json({error: 'Database error'});
    }
});

app.get('/api/printer', async (req, res) => {
    try{
        const [rows] = await db.query('SELECT * FROM printer');
        res.json(rows);
    } catch (err){
        console.error(err);
        res.status(500).json({error: 'Database error'});
    }
});

app.get('/api/printer3d', async (req, res) => {
    try{
        const [rows] = await db.query('SELECT * FROM printer3d');
        res.json(rows);
    } catch (err){
        console.error(err);
        res.status(500).json({error: 'Database error'});
    }
});

app.get('/api/room259', async (req, res) => {
    try{
        const [rows] = await db.query('SELECT * FROM room259');
        res.json(rows);
    } catch (err){
        console.error(err);
        res.status(500).json({error: 'Database error'});
    }
});

app.get('/api/room260', async (req, res) => {
    try{
        const [rows] = await db.query('SELECT * FROM room260');
        res.json(rows);
    } catch (err){
        console.error(err);
        res.status(500).json({error: 'Database error'});
    }
});

app.get('/api/users', async (req, res) => {
    try{
        const [rows] = await db.query('SELECT * FROM users');
        res.json(rows);
    } catch (err){
        console.error(err);
        res.status(500).json({error: 'Database error'});
    }
});

app.get('/api/room260-usageTime', async (req, res) => {
    try{
        const [rows] = await db.query(
            'SELECT HOUR(sessionlogin) AS hour, COUNT(*) AS sessions FROM room260 GROUP BY HOUR(sessionlogin) ORDER BY hour');
            res.json(rows);
    } catch (err) {
        console.error(err);
        res.status(500).json({error: 'Database error'});
    }
});

app.get('/api/room259-usageTime', async (req, res) => {
    try{
        const [rows] = await db.query(
            'SELECT HOUR(sessionlogin) AS hour, COUNT(*) AS sessions FROM room259 GROUP BY HOUR(sessionlogin) ORDER BY hour');
            res.json(rows);
    } catch (err) {
        console.error(err);
        res.status(500).json({error: 'Database error'});
    }
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running on http://localhost:${PORT}`))