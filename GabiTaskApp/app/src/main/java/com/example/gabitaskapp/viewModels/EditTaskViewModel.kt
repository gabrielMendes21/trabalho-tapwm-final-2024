package com.example.gabitaskapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gabitaskapp.models.Task
import com.example.gabitaskapp.repository.TaskRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class EditTaskViewModel : ViewModel() {
    private val _task = MutableLiveData<Task?>()
    val task: LiveData<Task?> get() = _task

    private val db = FirebaseFirestore.getInstance()

    fun loadTask(taskId: String) {
        viewModelScope.launch {
            try {
                val fetchedTask = TaskRepository(db).getTask(taskId)
                _task.value = fetchedTask
            } catch (e: Exception) {
                Log.e("EditTaskViewModel", "Erro ao taskregar tasko", e)
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                Log.d("EditTaskViewModel", "Atualizando tasko com ID: ${task.getId()}")
                TaskRepository(db).updateTask(task.getId().toString(), task)
            } catch (e: Exception) {
                Log.e("EditTaskViewModel", "Erro ao atualizar tasko", e)
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                TaskRepository(db).deleteTask(taskId)
            } catch (e: Exception) {
                Log.e("EditTaskViewModel", "Erro ao deletar tasko", e)
            }
        }
    }
}