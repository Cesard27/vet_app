const express = require('express');
const mysql = require('mysql2');
const multer = require('multer');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;

// MySQL setup
const db = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '',
  database: 'pets_db'
});

db.connect((err) => {
  if (err) {
    console.error('Error connecting to the database:', err.stack);
    return;
  }
  console.log('Connected to MySQL');
});

// Middleware to serve static files
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

// Multer setup for file upload
const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, 'uploads/')
  },
  filename: function (req, file, cb) {
    cb(null, Date.now() + path.extname(file.originalname))
  }
});

const upload = multer({ storage: storage });

// Middleware to parse JSON
app.use(express.json());

// Service 1: Store a pet
app.post('/pets', upload.single('image'), (req, res) => {
  const { type, name, age, breed } = req.body;
  const image = req.file.filename;

  const query = 'INSERT INTO pets (type, name, age, breed, image) VALUES (?, ?, ?, ?, ?)';
  db.query(query, [type, name, age, breed, image], (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }
    res.status(201).json({ message: 'Pet stored successfully' });
  });
});

// Service 2: Get all pets
app.get('/pets', (req, res) => {
  const query = 'SELECT * FROM pets';
  db.query(query, (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }

    const pets = results.map(pet => ({
      ...pet,
      image: `http://${req.headers.host}/uploads/${pet.image}`
    }));

    res.json(pets);
  });
});

// Service 3: Filter pets by name and sort by breed
app.get('/pets/filter', (req, res) => {
  const { name, sortBy } = req.query;
  let query = 'SELECT * FROM pets';
  const queryParams = [];

  if (name) {
    query += ' WHERE name LIKE ?';
    queryParams.push(`%${name}%`);
  }

  if (sortBy) {
    query += ` ORDER BY ${sortBy}`;
  } else {
    query += ' ORDER BY breed';
  }

  db.query(query, queryParams, (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }

    const pets = results.map(pet => ({
      ...pet,
      image: `http://${req.headers.host}/uploads/${pet.image}`
    }));

    res.json(pets);
  });
});

// Start server
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
