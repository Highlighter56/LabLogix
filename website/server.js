require('dotenv').config({
    path: __dirname + '/.env'
  });
const express = require('express');
const cors = require('cors');
const db = require("./db");

const app = express();

app.use(cors());
app.use(express.json());
app.use(express.static('public'));

app.get('/', (req, res) => {
    res.sendFile(__dirname + '/public/Home.html');
});

app.get('/room260', (req, res) => {
    res.sendFile(__dirname + '/public/Room260.html');
});

app.get('/room259', (req, res) => {
    res.sendFile(__dirname + '/public/Room259.html');
});

app.get('/home', (req, res) => {
    res.sendFile(__dirname + '/public/Home.html');
});

app.get('/259Style.css', (req, res) => {
    res.sendFile(__dirname + '/public/259Style.css');
});

app.get('/260Style.css', (req, res) => {
    res.sendFile(__dirname + '/public/260Style.css');
});

app.get('/homeStyle.css', (req, res) => {
    res.sendFile(__dirname + '/public/homeStyle.css');
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

app.get('/api/room259-current', async (req, res) => {
    try {
        const [rows] = await db.query(
            'SELECT * FROM room259 ORDER BY sessionlogin DESC, room259ID DESC LIMIT 1'
        );
        res.json(rows[0] || null);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Database error' });
    }
});

app.get('/api/room260-current', async (req, res) => {
    try {
        const [rows] = await db.query(
            'SELECT * FROM room260 ORDER BY sessionlogin DESC, room260ID DESC LIMIT 1'
        );
        res.json(rows[0] || null);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Database error' });
    }
});

app.get('/api/room259-devices', async (req, res) => {
    try {
        const [rows] = await db.query(
            "SELECT 'PC' AS deviceType, pcID AS deviceId, status FROM pc WHERE room259ID IS NOT NULL " +
            "UNION ALL " +
            "SELECT 'Printer' AS deviceType, printerID AS deviceId, status FROM printer"
        );
        res.json(rows);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Database error' });
    }
});

app.get('/api/room260-devices', async (req, res) => {
    try {
        const [rows] = await db.query(
            "SELECT 'PC' AS deviceType, pcID AS deviceId, status FROM pc WHERE room260ID IS NOT NULL " +
            "UNION ALL " +
            "SELECT '3D Printer' AS deviceType, printer3dID AS deviceId, status FROM printer3d"
        );
        res.json(rows);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Database error' });
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

app.get('/api/notificationTable', async (req, res) => {
    try {
        let rows;
        try {
            [rows] = await db.query('SELECT notificationID, status FROM notification');
        } catch (primaryErr) {
            [rows] = await db.query('SELECT notificationID, status FROM notificationTable');
        }
        res.json(rows);
    } catch (err) {
        console.error(err);
        res.status(500).json({error: 'Database error'});
    }
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running on http://localhost:${PORT}`))