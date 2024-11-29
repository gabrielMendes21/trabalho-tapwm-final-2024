package com.example.gabitaskapp.models

data class Task(
    private var id: String? = null,
    private var title: String,
    private var completed: Boolean,
    private var userId: String
) {
    fun getId(): String? {
        return id
    }

    fun setId(novoId: String) {
        id = novoId
    }

    fun getTitle(): String {
        return title
    }

    fun setTitle(novoTitulo: String) {
        title = novoTitulo
    }

    fun getCompleted(): Boolean {
        return completed
    }

    fun setCompleted(novoStatus: Boolean) {
        completed = novoStatus
    }

    fun getUserId(): String {
        return userId
    }

    fun setUserId(novoIdUsuario: String) {
        userId = novoIdUsuario
    }
}