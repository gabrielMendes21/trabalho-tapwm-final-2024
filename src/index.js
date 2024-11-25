const express = require('express');
const { engine } = require('express-handlebars');
const { join } = require('path');
const admin = require("./lib/admin.js");
const todoRoutes = require("./routes/todo.js");


// Initialize Express
const app = express();
app.use(express.json());
app.use(express.static("src"));
app.use(express.static("public"));

// TODO CRUD
app.use(todoRoutes);

// Handlebars configuration
app.engine('handlebars', engine());
app.set('view engine', 'handlebars');
app.set('views', join(__dirname, "views"));

app.get("/", (req, res) => {
    res.render("home");
})

// Login route (GET)
app.get("/login", (req, res) => {
    res.render("login");
});

// Register Route
app.get("/register", async (req, res) => {
    res.render("register");
});

app.listen(3000, () => console.log("Server running on http://localhost:3000"));
