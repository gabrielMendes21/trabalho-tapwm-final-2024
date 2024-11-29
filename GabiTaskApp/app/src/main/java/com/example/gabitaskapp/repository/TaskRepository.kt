package com.example.gabitaskapp.repository

import android.util.Log
import com.example.gabitaskapp.models.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TaskRepository(private val db: FirebaseFirestore) {
    private val tasksCollection = db.collection("tasks")

    suspend fun addTask(task: Task): Boolean {
        return try {
            val newTaskRef = tasksCollection.add(
                mapOf(
                    "title" to task.getTitle(),
                    "completed" to task.getCompleted(),
                    "userId" to task.getUserId()
                )
            ).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getTasks(userId: String): List<Task> {
        return try {
            val snapshot = tasksCollection.whereEqualTo("userId", userId).get().await()
            snapshot.documents.map { document ->
                Task(
                    id = document.id,
                    title = document.getString("title") ?: "",
                    completed = document.getBoolean("completed") ?: false,
                    userId = document.getString("userId") ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getTask(id: String): Task? {
        return try {
            val document = tasksCollection.document(id).get().await()
            if (document.exists()) {
                Task(
                    id = document.id,
                    title = document.getString("title") ?: "",
                    completed = document.getBoolean("completed") ?: false,
                    userId = document.getString("userId") ?: ""
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateTask(id: String, task: Task?): Boolean {
        return try {
            val documentRef = tasksCollection.document(id)
            documentRef.update(
                "title", task?.getTitle(),
                "completed", task?.getCompleted(),
                "userId", task?.getUserId()
            ).await()
            true
        } catch (e: Exception) {
            Log.e("TaskRepository", "Erro ao atualizar task: ${e.message}")
            false
        }
    }

    suspend fun deleteTask(id: String): Boolean {
        return try {
            tasksCollection.document(id).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}