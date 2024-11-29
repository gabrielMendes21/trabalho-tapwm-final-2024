package com.example.gabitaskapp.ui.tasks

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.gabitaskapp.repository.TaskRepository
import com.example.gabitaskapp.ui.components.CustomTextField
import com.example.gabitaskapp.ui.components.TopAppBar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    onNavigateToListTasks: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user = FirebaseAuth.getInstance().currentUser
    val db = Firebase.firestore

    user?.let {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
            CreateTaskBody(db, onNavigateToListTasks, innerPadding)
        }
    }
}

@Composable
fun CreateTaskBody(
    db: FirebaseFirestore,
    onNavigateToListTasks: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
){
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(contentPadding)
    ) {
        TaskInputForm(db, onNavigateToListTasks)
    }
}

@Composable
fun TaskInputForm(
    db: FirebaseFirestore,
    onNavigateToListTasks: () -> Unit,
    task: Task? = null,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(task?.getTitle() ?: "") }
    var completed by remember { mutableStateOf(task?.getCompleted() ?: false) }
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomTextField(
            value = title,
            onValueChange = { title = it },
            label = "Title",
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                completed = !completed
                Log.d("Completed Variable","completed: $completed")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = if (completed) "Desmarcar" else "Marcar", color = MaterialTheme.colorScheme.secondary)
        }

        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if(title.isEmpty()){
                    Toast.makeText(context, "Preencha todos os Campos!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                coroutineScope.launch {
                    try {
                        val task = Task(null, title, completed, user?.uid.toString())
                        val result = TaskRepository(db).addTask(task)

                        Toast.makeText(context, "Task Criado com Sucesso!", Toast.LENGTH_SHORT).show()
                        onNavigateToListTasks()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Erro ao Criar Taskro!", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Salvar", color = MaterialTheme.colorScheme.secondary)
        }
    }
}