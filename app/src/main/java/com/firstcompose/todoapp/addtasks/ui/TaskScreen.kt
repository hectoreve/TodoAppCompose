package com.firstcompose.todoapp.addtasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.firstcompose.todoapp.addtasks.ui.model.TaskModel
import com.firstcompose.todoapp.ui.theme.TodoAppTheme

@Composable
fun TasksScreen(taskViewModel: TasksViewModel = viewModel()) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val showDialog by taskViewModel.showDialog.observeAsState(false)

    val uiState by produceState<TaskUiState>(
        initialValue = TaskUiState.Loading,
        key1 = lifecycle,
        key2 = taskViewModel
    ) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            taskViewModel.uiState.collect { value = it}
        }
    }

    when(uiState){
        is TaskUiState.Error -> {}
        TaskUiState.Loading -> {}
        is TaskUiState.Success -> {
            Box(Modifier.fillMaxSize()) {
                AddTaskDialog(showDialog, onDismiss = { taskViewModel.onDialogClose() }, onTaskAdded =
                { taskViewModel.onTaskCreated(it) })
                FabDialog(Modifier.align(Alignment.BottomEnd), taskViewModel)
                TasksList((uiState as TaskUiState.Success).tasks, taskViewModel)
            }
        }
    }
}

@Composable
fun TasksList(tasks: List<TaskModel>, taskViewModel: TasksViewModel) {
    LazyColumn(content = {
        items(tasks, key = { it.id }) { task ->
            ItemTask(taskModel = task, taskViewModel = taskViewModel)
        }
    })
}

@Composable
fun ItemTask(taskModel: TaskModel, taskViewModel: TasksViewModel) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = { taskViewModel.onItemRemove(taskModel) })
            },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(taskModel.task, modifier = Modifier.weight(1f))
            Checkbox(checked = taskModel.selected,
                onCheckedChange = {
                    taskViewModel.onCheckBoxSelect(taskModel)
                })
        }
    }

}

@Composable
fun AddTaskDialog(show: Boolean, onDismiss: () -> Unit, onTaskAdded: (String) -> Unit) {
    var myTask by remember {
        mutableStateOf("")
    }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    "Añadir tu tarea",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = myTask, onValueChange = { myTask = it }, singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    onTaskAdded(myTask)
                    myTask = ""
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Añadir Tarea")
                }
            }
        }
    }
}

@Composable
fun FabDialog(modifier: Modifier, taskViewModel: TasksViewModel) {
    FloatingActionButton(onClick = {
        taskViewModel.onShowDialogClick()
    }, modifier = modifier) {
        Icon(Icons.Filled.Add, contentDescription = "")
    }
}

@Preview(showBackground = true)
@Composable
fun TasksScreenPreview() {
    TodoAppTheme {
        TasksScreen()
    }
}

@Preview(name = "Dialog Precview")
@Composable
fun DialogPreview() {
    TodoAppTheme {
        AddTaskDialog(show = true, onDismiss = { }, onTaskAdded = {})
    }
}