package com.firstcompose.todoapp.addtasks.domain

import com.firstcompose.todoapp.addtasks.data.TasksRepository
import com.firstcompose.todoapp.addtasks.ui.model.TaskModel
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    suspend operator fun invoke(task: TaskModel){
        repository.updateTask(task)
    }
}