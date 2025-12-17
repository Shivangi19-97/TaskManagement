package com.taskmanagement.ui

import androidx.lifecycle.LiveData
import com.taskmanagement.database.TaskDao
import com.taskmanagement.database.TaskEntity

class TaskRepository(private val dao: TaskDao) {

    val allTasks: LiveData<List<TaskEntity>> = dao.getAllTasks()

    suspend fun insert(task: TaskEntity) = dao.insertTask(task)

    suspend fun update(task: TaskEntity) = dao.updateTask(task)

    suspend fun delete(task: TaskEntity) = dao.deleteTask(task)

    fun search(query: String): LiveData<List<TaskEntity>> =
        dao.searchTasks("%$query%")
}
