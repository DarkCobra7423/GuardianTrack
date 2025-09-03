require('dotenv').config();

const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const app = express();

// Middleware
app.use(cors());
app.use(express.json());

// MongoDB Connection
mongoose.connect(process.env.MONGODB_URI)
  .then(() => {
    console.log('✅ MongoDB connected');
    app.listen(port, () => {
      console.log(`🌐 API listening on port ${port}`);
      console.log(`🟢 The Guardian Track Online`);
    });
  })
  .catch((error) => {
    console.error('❌ Failed to connect to MongoDB:', error);
  });

// Define Models

// User Model
const User = mongoose.model('User', new mongoose.Schema({
  name: String,
  email: String,
  createdAt: { type: Date, default: Date.now }
}));

// SOS Alert Model
const SosAlert = mongoose.model('SosAlert', new mongoose.Schema({
  userId: mongoose.Schema.Types.ObjectId,
  lat: Number,
  lng: Number,
  timestamp: { type: Date, default: Date.now }
}));

// Missing Person Model
const MissingPersonSchema = new mongoose.Schema({
  userId: String,
  folio: String,
  full_name: String,
  curp: String,
  age: Number,
  gender: String,
  state: String,
  city: String,
  missing_date: Date,
  protocol: String,
  status: String,             // "Missing" or "Found"
  condition: String,          // "Alive", "Deceased", or null
  photo_urls: [String],       // Array of photo URLs
  description: String,
  complexion: String,
  reporter: String,
  notes: String,
  created_at: { type: Date, default: Date.now }
});

const MissingPerson = mongoose.model('MissingPerson', MissingPersonSchema);

// Routes

// Create new user
app.post('/users', async (req, res) => {
  try {
    const user = await new User(req.body).save();
    res.status(201).json(user);
  } catch (error) {
    res.status(400).json({ error: 'Failed to create user' });
  }
});

// Create SOS alert
app.post('/sos', async (req, res) => {
  try {
    console.log("🚨 SOS received:", req.body);
    const alert = await new SosAlert(req.body).save();
    res.status(201).json(alert);
  } catch (error) {
    res.status(400).json({ error: 'Failed to register SOS alert' });
  }
});

// Create new missing person report
app.post('/missing', async (req, res) => {
  try {
    const person = await new MissingPerson(req.body).save();
    res.status(201).json(person);
  } catch (error) {
    res.status(400).json({ error: 'Failed to create report' });
  }
});

// Get all missing persons
app.get('/missing', async (req, res) => {
  try {
    const persons = await MissingPerson.find().sort({ created_at: -1 });
    res.json(persons);
  } catch (error) {
    res.status(500).json({ error: 'Failed to retrieve data' });
  }
});

// Get missing person by folio
app.get('/missing/:folio', async (req, res) => {
  try {
    const person = await MissingPerson.findOne({ folio: req.params.folio });
    if (!person) return res.status(404).json({ error: 'Not found' });
    res.json(person);
  } catch (error) {
    res.status(500).json({ error: 'Error searching folio' });
  }
});

// Delete missing person report by folio
app.delete('/missing/:folio', async (req, res) => {
  try {
    const result = await MissingPerson.deleteOne({ folio: req.params.folio });
    if (result.deletedCount === 0) {
      return res.status(404).json({ error: 'Not found to delete' });
    }
    res.json({ message: 'Successfully deleted' });
  } catch (error) {
    res.status(500).json({ error: 'Failed to delete' });
  }
});

// Define Port
const port = process.env.PORT || 3000;
