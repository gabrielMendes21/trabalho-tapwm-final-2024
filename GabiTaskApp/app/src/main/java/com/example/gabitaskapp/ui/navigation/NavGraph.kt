package com.example.gabitaskapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.example.gabitaskapp.ui.auth.LoginScreen
import com.example.gabitaskapp.ui.auth.RegisterScreen
import com.example.gabitaskapp.ui.auth.DashboardScreen
import com.example.gabitaskapp.ui.tasks.CreateTaskScreen
import com.example.gabitaskapp.ui.tasks.EditTaskScreen
import com.example.gabitaskapp.ui.tasks.ListTasksScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    NavHost(navController, startDestination = if (currentUser != null) "dashboard" else "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login")
                }
            )
        }
        composable("dashboard") {
            DashboardScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                onNavigateToListTasks = {
                    navController.navigate("listTasks")
                }
            )
        }
        composable("listTasks") {
            ListTasksScreen(
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                onNavigateToCreateTask = {
                    navController.navigate("createTask") {
                        popUpTo("createTask") { inclusive = true }
                    }
                },
                onNavigateToTaskUpdate = { taskId ->
                    navController.navigate("editTask/$taskId")
                }
            )
        }
        composable("createTask") {
            CreateTaskScreen(
                onNavigateToListTasks = {
                    navController.navigate("listTasks") {
                        popUpTo("listTasks") { inclusive = true }
                    }
                }
            )
        }
        composable("editTask/{taskId}") {
            EditTaskScreen(
                onNavigateToListTasks = {
                    navController.navigate("listTasks") {
                        popUpTo("listTasks") { inclusive = true }
                    }
                },
                taskId = it.arguments?.getString("taskId") ?: ""
            )
        }
    }
}