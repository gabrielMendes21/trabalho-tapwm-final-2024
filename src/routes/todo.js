const express = require('express');
const verifyToken = require('../verifyToken');
const router = express.Router();

const db = require("../lib/admin.js").firestore();

// Middleware de autenticação
const authMiddleware = async (req, res, next) => {
    const token = req.headers['authorization']?.split(' ')[1]; 

    if (!token) {
        return res.status(401).json({ message: 'Unauthorized' });
    }

    const userId = await verifyToken(token);

    if (!userId) {
        return res.status(401).json({ message: 'Invalid token' });
    }

    req.userId = userId;
    next();
};

// Get all user's tasks
router.get('/tasks', authMiddleware, async (req, res) => {
    try {
        const tasksSnapshot = await db.collection('tasks').where('userId', '==', req.userId).get();
        if (tasksSnapshot.empty) {
            return res.status(404).json({ message: 'No tasks found' });
        }


        const tasks = tasksSnapshot.docs.map(doc => ({
            id: doc.id, // ID do documento
            ...doc.data() // Dados do documento
        }));

        res.json(tasks);
    } catch (error) {
        res.status(500).json({ message: 'Error fetching tasks', error });
    }
});

// Add task
router.post('/tasks', authMiddleware, async (req, res) => {
    const { title } = req.body;
    const newTask = {
        title,
        completed: false,
        userId: req.userId,
    };

    try {
        const taskRef = await db.collection('tasks').add(newTask);
        res.status(201).json({ success: true, id: taskRef.id });
    } catch (error) {
        res.status(500).json({ message: 'Error adding task', error });
    }
});

// Update task
router.put('/tasks/:id', authMiddleware, async (req, res) => {
    const taskId = req.params.id;
    const { title } = req.body;

    try {
        const taskDoc = await db.collection('tasks').doc(taskId).get();
        console.log(taskDoc)

        if (!taskDoc.exists || taskDoc.data().userId !== req.userId) {
            return res.status(404).json({ success: false, message: 'Task not found or unauthorized' });
        }

        await taskDoc.ref.update({ title });
        res.json({ success: true });
    } catch (error) {
        res.status(500).json({ message: 'Error updating task', error });
    }
});

// Toggle task checkbokx
router.put('/toggleTask/:id', authMiddleware, async (req, res) => {
    const taskId = req.params.id;
    const { completed } = req.body;

    try {
        const taskDoc = await db.collection('tasks').doc(taskId).get();
        console.log(taskDoc)

        if (!taskDoc.exists || taskDoc.data().userId !== req.userId) {
            return res.status(404).json({ success: false, message: 'Task not found or unauthorized' });
        }

        await taskDoc.ref.update({ completed });
        res.json({ success: true });
    } catch (error) {
        res.status(500).json({ message: 'Error updating task', error });
    }
});

// Remove task
router.delete('/tasks/:id', authMiddleware, async (req, res) => {
    const taskId = req.params.id;

    try {
        const taskDoc = await db.collection('tasks').doc(taskId).get();

        if (!taskDoc.exists || taskDoc.data().userId !== req.userId) {
            return res.status(404).json({ success: false, message: 'Task not found or unauthorized' });
        }

        await taskDoc.ref.delete();
        res.json({ success: true });
    } catch (error) {
        res.status(500).json({ message: 'Error deleting task', error });
    }
});

module.exports = router;
