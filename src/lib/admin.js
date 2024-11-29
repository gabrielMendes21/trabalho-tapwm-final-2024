const admin = require("firebase-admin");
require("dotenv").config();
const serviceAccount = require("../../firebase-secret-key.json");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
});

module.exports = admin;