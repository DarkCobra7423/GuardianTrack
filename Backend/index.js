// index.js
require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json());

mongoose.connect(process.env.MONGODB_URI, {
  useNewUrlParser: true,
  useUnifiedTopology: true
}).then(() => console.log('✅ MongoDB conectado'));

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

app.post('/users', async (req, res) => {
  const user = await new User(req.body).save();
  res.status(201).json(user);
});
/*
app.post('/sos', async (req, res) => {
  const alert = await new SosAlert(req.body).save();
  res.status(201).json(alert);
});*/

app.post('/sos', async (req, res) => {
  console.log("BODY RECIBIDO:", req.body); // 👈 Aquí
  const alert = await new SosAlert(req.body).save();
  res.status(201).json(alert);
});


const port = process.env.PORT || 3000;
app.listen(port, () => console.log(`🌐 API escuchando en puerto ${port}`));
