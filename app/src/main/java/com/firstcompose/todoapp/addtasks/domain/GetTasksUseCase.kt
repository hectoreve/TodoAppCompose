package com.firstcompose.todoapp.addtasks.domain

import com.firstcompose.todoapp.addtasks.data.TasksRepository
import com.firstcompose.todoapp.addtasks.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {

    operator fun invoke(): Flow<List<TaskModel>>  = tasksRepository.tasks

}