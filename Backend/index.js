require('dotenv').config();

const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const app = express();
const port = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(express.json());

// MongoDB Connection
mongoose.connect(process.env.MONGODB_URI)
  .then(() => {
    console.log('✅ MongoDB connected');
    app.listen(port, () => {
      console.log(`🌐 API listening on port ${port}`);
      console.log(`🟢 The Guardian Track Is Online`);
    });
  })
  .catch((error) => {
    console.error('❌ Failed to connect to MongoDB:', error);
  });

// Models
const User = mongoose.model('User', new mongoose.Schema({
  name: String,
  email: String,
  createdAt: { type: Date, default: Date.now }
}));

const SosAlert = mongoose.model('SosAlert', new mongoose.Schema({
  userId: mongoose.Schema.Types.ObjectId,
  lat: Number,
  lng: Number,
  timestamp: { type: Date, default: Date.now }
}));

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
  status: String,
  condition: String,
  photo_urls: [String],
  description: String,
  complexion: String,
  reporter: String,
  notes: String,
  created_at: { type: Date, default: Date.now }
});

const MissingPerson = mongoose.model('MissingPerson', MissingPersonSchema);

// 🔹 Emergency Contacts Model
const EmergencyContact = mongoose.model('EmergencyContact', new mongoose.Schema({
  ownerCurp: { type: String, required: true },      // el que agrega
  contactCurp: { type: String, required: true },    // el agregado
  addedAt: { type: Date, default: Date.now }
}));

// Routes

// 🔸 Create new user
app.post('/users', async (req, res) => {
  try {
    const user = await new User(req.body).save();
    res.status(201).json(user);
  } catch (error) {
    res.status(400).json({ error: 'Failed to create user' });
  }
});

// 🔸 Create SOS alert
app.post('/sos', async (req, res) => {
  try {
    console.log("🚨 SOS received:", req.body);
    const alert = await new SosAlert(req.body).save();
    res.status(201).json(alert);
  } catch (error) {
    res.status(400).json({ error: 'Failed to register SOS alert' });
  }
});

// 🔸 Create new missing person report
app.post('/missing', async (req, res) => {
  try {
    const person = await new MissingPerson(req.body).save();
    res.status(201).json(person);
  } catch (error) {
    res.status(400).json({ error: 'Failed to create report' });
  }
});

// 🔸 Get all missing persons
app.get('/missing', async (req, res) => {
  try {
    const persons = await MissingPerson.find().sort({ created_at: -1 });
    res.json(persons);
  } catch (error) {
    res.status(500).json({ error: 'Failed to retrieve data' });
  }
});

// 🔸 Get missing person by folio
app.get('/missing/:folio', async (req, res) => {
  try {
    const person = await MissingPerson.findOne({ folio: req.params.folio });
    if (!person) return res.status(404).json({ error: 'Not found' });
    res.json(person);
  } catch (error) {
    res.status(500).json({ error: 'Error searching folio' });
  }
});

// 🔸 Delete missing person report by folio
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

// 🔹 Emergency Contact Routes

// ➕ Add contact
app.post('/contacts', async (req, res) => {
  const { ownerCurp, contactCurp } = req.body;

  if (!ownerCurp || !contactCurp) {
    return res.status(400).json({ error: 'Both ownerCurp and contactCurp are required' });
  }

  try {
    const exists = await EmergencyContact.findOne({ ownerCurp, contactCurp });
    if (exists) return res.status(409).json({ error: 'Contact already exists' });

    const contact = await EmergencyContact.create({ ownerCurp, contactCurp });
    res.status(201).json(contact);
  } catch (error) {
    res.status(500).json({ error: 'Error adding contact' });
  }
});

// ❌ Remove contact
app.delete('/contacts', async (req, res) => {
  const { ownerCurp, contactCurp } = req.body;

  if (!ownerCurp || !contactCurp) {
    return res.status(400).json({ error: 'Both ownerCurp and contactCurp are required' });
  }

  try {
    const result = await EmergencyContact.deleteOne({ ownerCurp, contactCurp });
    if (result.deletedCount === 0) {
      return res.status(404).json({ error: 'Contact not found' });
    }
    res.json({ message: 'Contact deleted' });
  } catch (error) {
    res.status(500).json({ error: 'Error deleting contact' });
  }
});

// 📥 Get contacts added by me
app.get('/contacts/:curp', async (req, res) => {
  const curp = req.params.curp;
  try {
    const contacts = await EmergencyContact.find({ ownerCurp: curp });
    const contactDetails = await MissingPerson.find({ curp: { $in: contacts.map(c => c.contactCurp) } });
    res.json(contactDetails);
  } catch (error) {
    res.status(500).json({ error: 'Error fetching my contacts' });
  }
});

// 👥 Get people who have me as contact
app.get('/contacts/me/:curp', async (req, res) => {
  const curp = req.params.curp;
  try {
    const owners = await EmergencyContact.find({ contactCurp: curp });
    const ownerDetails = await MissingPerson.find({ curp: { $in: owners.map(c => c.ownerCurp) } });
    res.json(ownerDetails);
  } catch (error) {
    res.status(500).json({ error: 'Error fetching contacts who added me' });
  }
});
