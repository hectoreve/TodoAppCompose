package com.firstcompose.todoapp.addtasks.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.firstcompose.todoapp.addtasks.data.TaskDao
import com.firstcompose.todoapp.addtasks.data.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext appContext: Context): TodoDatabase =
        Room.databaseBuilder(appContext, TodoDatabase::class.java, "TaskDatabase").build()

    @Provides
    fun providesTaskDao(todoDatabase: TodoDatabase): TaskDao =
        todoDatabase.taskDao()
}