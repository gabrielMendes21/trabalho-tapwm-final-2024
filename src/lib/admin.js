const admin = require("firebase-admin");
require("dotenv").config();
// const serviceAccount = require("../../trabalho-final-tapwm-firebase-adminsdk-7gbwr-310c4892e5.json");

const serviceAccount = JSON.parse(process.env.FIREBASE_CONFIG);

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
});

module.exports = admin;