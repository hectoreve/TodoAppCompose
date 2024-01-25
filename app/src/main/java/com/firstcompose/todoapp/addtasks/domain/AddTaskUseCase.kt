package com.firstcompose.todoapp.addtasks.domain

import com.firstcompose.todoapp.addtasks.data.TaskDao
import com.firstcompose.todoapp.addtasks.data.TasksRepository
import com.firstcompose.todoapp.addtasks.ui.model.TaskModel
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
){
    suspend operator fun invoke(task: TaskModel) {
        tasksRepository.addTask(task)
    }
}