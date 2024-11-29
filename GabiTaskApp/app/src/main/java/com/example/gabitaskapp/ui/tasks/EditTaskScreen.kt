package com.example.gabitaskapp.ui.tasks

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gabitaskapp.R
import com.example.gabitaskapp.models.Task
import com.example.gabitaskapp.ui.components.CustomTextField
import com.example.gabitaskapp.ui.components.DialogButton
import com.example.gabitaskapp.ui.components.TopAppBar
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gabitaskapp.viewModels.EditTaskViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    onNavigateToListTasks: () -> Unit,
    taskId: String,
    modifier: Modifier = Modifier,
    viewModel: EditTaskViewModel = viewModel()
) {
    val task by viewModel.task.observeAsState(null)

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    if (task != null) {
        Scaffold(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = stringResource(id = R.string.app_name),
                    canNavigateBack = true,
                    scrollBehavior = scrollBehavior,
                    navigateUp = onNavigateToListTasks
                )
            },
            contentColor = MaterialTheme.colorScheme.secondary,
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            EditTaskBody(task, onNavigateToListTasks, viewModel, innerPadding)
        }
    }
}

@Composable
fun EditTaskBody(
    task: Task?,
    onNavigateToListTasks: () -> Unit,
    viewModel: EditTaskViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(contentPadding)
    ) {
        TaskEditInputForm(task, onNavigateToListTasks, viewModel)
    }
}

@Composable
fun TaskEditInputForm(
    task: Task?,
    onNavigateToListTasks: () -> Unit,
    viewModel: EditTaskViewModel,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(task?.getTitle() ?: "") }
    var completed by remember { mutableStateOf(task?.getCompleted() ?: false) }
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTextField(
            value = title,
            onValueChange = { title = it },
            label = "Title",
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                task?.let {
                    completed = !completed
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = if (completed) "Desmarcar" else "Marcar", color = MaterialTheme.colorScheme.secondary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (title.isEmpty()) {
                    Toast.makeText(context, "Preencha todos os Campos!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val updatedTask = task?.apply {
                    setTitle(title)
                    setCompleted(completed)
                    setUserId(user?.uid.toString())
                }
                updatedTask?.let {
                    viewModel.updateTask(it)
                    Toast.makeText(context, "Taskro atualizado com sucesso", Toast.LENGTH_SHORT).show()
                    onNavigateToListTasks()
                }
            }
        ) {
            Text(text = "Salvar", color = MaterialTheme.colorScheme.secondary)
        }

        DialogButton(
            label = "Deletar",
            title = "Confirmar Exclusão",
            message = "Você tem certeza de que deseja deletar este taskro?",
            icon = {
                Icon(
                    androidx.compose.material.icons.Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.secondary
                )
            },
            onConfirm = {
                task?.getId()?.let { taskId ->
                    viewModel.deleteTask(taskId)
                    Toast.makeText(context, "Taskro deletado com sucesso", Toast.LENGTH_SHORT).show()
                    onNavigateToListTasks()
                }
            }
        )
    }
}