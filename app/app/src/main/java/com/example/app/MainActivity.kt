package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import com.example.app.ui.theme.ResourcesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainApp()
            }
        }
    }
}

data class Task(val id: Int, val title: String, val category: String = "", var isCompleted: Boolean = false)

@Composable
fun MainApp() {
    var currentScreen by remember { mutableStateOf("home") }

    val doNowTasks = remember { mutableStateListOf<Task>() }
    val doLaterTasks = remember { mutableStateListOf<Task>() }
    val handOffTasks = remember { mutableStateListOf<Task>() }
    val skipTasks = remember { mutableStateListOf<Task>() }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentScreen = currentScreen,
                onScreenChange = { currentScreen = it }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                "home" -> HomeScreen(
                    doNowTasks = doNowTasks,
                    doLaterTasks = doLaterTasks,
                    handOffTasks = handOffTasks,
                    onViewAllClick = { currentScreen = "todo" }
                )
                "todo" -> EisenhowerMatrixScreen(
                    doNowTasks = doNowTasks,
                    doLaterTasks = doLaterTasks,
                    handOffTasks = handOffTasks,
                    skipTasks = skipTasks
                )
                "resources" -> ResourcesScreen()
                else -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Coming Soon", fontSize = 24.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentScreen: String,
    onScreenChange: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentScreen == "home",
            onClick = { onScreenChange("home") },
            icon = { Icon(Icons.Default.Home, "Home") },
            label = { Text("Home", fontSize = 12.sp) }
        )

        NavigationBarItem(
            selected = currentScreen == "todo",
            onClick = { onScreenChange("todo") },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "To-Do"
                )
            },
            label = { Text("To-Do", fontSize = 12.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                indicatorColor = Color(0xFF6366F1)
            )
        )

        NavigationBarItem(
            selected = currentScreen == "analytics",
            onClick = { onScreenChange("analytics") },
            icon = { Icon(Icons.Outlined.BarChart, "Analytics") },
            label = { Text("Analytics", fontSize = 12.sp) }
        )

        NavigationBarItem(
            selected = currentScreen == "friends",
            onClick = { onScreenChange("friends") },
            icon = { Icon(Icons.Outlined.People, "Friends") },
            label = { Text("Friends", fontSize = 12.sp) }
        )

        NavigationBarItem(
            selected = currentScreen == "resources",
            onClick = { onScreenChange("resources") },
            icon = { Icon(Icons.Outlined.Book, "Resources") },
            label = { Text("Resources", fontSize = 12.sp) }
        )
    }
}