package com.taskmanagement.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.taskmanagement.database.TaskDatabase
import com.taskmanagement.database.TaskEntity
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository
    val tasks: LiveData<List<TaskEntity>>

    init {
        val dao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(dao)
        tasks = repository.allTasks
    }

    fun insert(title: String, desc: String) {
        if (title.isBlank() || desc.isBlank()) return
        viewModelScope.launch {
            repository.insert(TaskEntity(title = title, description = desc))
        }
    }

    fun update(task: TaskEntity) = viewModelScope.launch {
        repository.update(task)
    }

    fun delete(task: TaskEntity) = viewModelScope.launch {
        repository.delete(task)
    }

    fun search(query: String): LiveData<List<TaskEntity>> =
        repository.search(query)
}