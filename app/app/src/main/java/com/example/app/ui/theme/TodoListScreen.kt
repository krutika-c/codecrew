package com.example.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun EisenhowerMatrixScreen(
    doNowTasks: SnapshotStateList<Task>,
    doLaterTasks: SnapshotStateList<Task>,
    handOffTasks: SnapshotStateList<Task>,
    skipTasks: SnapshotStateList<Task>
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedQuadrant by remember { mutableStateOf("") }
    var showQuadrantSelector by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showQuadrantSelector = true },
                containerColor = Color(0xFF6366F1),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Eisenhower Matrix",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )

            Text(
                text = "Prioritize what matters most",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuadrantCard(
                        title = "Do Now",
                        subtitle = "gotta do it",
                        color = Color(0xFFEF4444),
                        tasks = doNowTasks,
                        onTaskToggle = { taskId ->
                            val index = doNowTasks.indexOfFirst { it.id == taskId }
                            if (index != -1) {
                                doNowTasks[index] = doNowTasks[index].copy(isCompleted = !doNowTasks[index].isCompleted)
                            }
                        },
                        onAddTask = {
                            selectedQuadrant = "Do Now"
                            showAddDialog = true
                        },
                        modifier = Modifier.weight(1f)
                    )

                    QuadrantCard(
                        title = "Do Later",
                        subtitle = "when you have time",
                        color = Color(0xFF3B82F6),
                        tasks = doLaterTasks,
                        onTaskToggle = { taskId ->
                            val index = doLaterTasks.indexOfFirst { it.id == taskId }
                            if (index != -1) {
                                doLaterTasks[index] = doLaterTasks[index].copy(isCompleted = !doLaterTasks[index].isCompleted)
                            }
                        },
                        onAddTask = {
                            selectedQuadrant = "Do Later"
                            showAddDialog = true
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuadrantCard(
                        title = "Hand Off",
                        subtitle = "not just you",
                        color = Color(0xFFFBBF24),
                        tasks = handOffTasks,
                        onTaskToggle = { taskId ->
                            val index = handOffTasks.indexOfFirst { it.id == taskId }
                            if (index != -1) {
                                handOffTasks[index] = handOffTasks[index].copy(isCompleted = !handOffTasks[index].isCompleted)
                            }
                        },
                        onAddTask = {
                            selectedQuadrant = "Hand Off"
                            showAddDialog = true
                        },
                        modifier = Modifier.weight(1f)
                    )

                    QuadrantCard(
                        title = "Skip",
                        subtitle = "let it go",
                        color = Color(0xFF10B981),
                        tasks = skipTasks,
                        onTaskToggle = { taskId ->
                            val index = skipTasks.indexOfFirst { it.id == taskId }
                            if (index != -1) {
                                skipTasks[index] = skipTasks[index].copy(isCompleted = !skipTasks[index].isCompleted)
                            }
                        },
                        onAddTask = {
                            selectedQuadrant = "Skip"
                            showAddDialog = true
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    if (showQuadrantSelector) {
        QuadrantSelectorDialog(
            onDismiss = { showQuadrantSelector = false },
            onQuadrantSelected = { quadrant ->
                selectedQuadrant = quadrant
                showQuadrantSelector = false
                showAddDialog = true
            }
        )
    }

    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { taskTitle ->
                val newTask = Task(
                    id = System.currentTimeMillis().toInt(),
                    title = taskTitle,
                    category = selectedQuadrant
                )
                when (selectedQuadrant) {
                    "Do Now" -> doNowTasks.add(newTask)
                    "Do Later" -> doLaterTasks.add(newTask)
                    "Hand Off" -> handOffTasks.add(newTask)
                    "Skip" -> skipTasks.add(newTask)
                }
                showAddDialog = false
            }
        )
    }
}

@Composable
fun QuadrantSelectorDialog(
    onDismiss: () -> Unit,
    onQuadrantSelected: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Select Category",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                Spacer(modifier = Modifier.height(16.dp))

                QuadrantOption("Do Now", Color(0xFFEF4444)) {
                    onQuadrantSelected("Do Now")
                }

                Spacer(modifier = Modifier.height(12.dp))

                QuadrantOption("Do Later", Color(0xFF3B82F6)) {
                    onQuadrantSelected("Do Later")
                }

                Spacer(modifier = Modifier.height(12.dp))

                QuadrantOption("Hand Off", Color(0xFFFBBF24)) {
                    onQuadrantSelected("Hand Off")
                }

                Spacer(modifier = Modifier.height(12.dp))

                QuadrantOption("Skip", Color(0xFF10B981)) {
                    onQuadrantSelected("Skip")
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cancel", color = Color(0xFF6B7280))
                }
            }
        }
    }
}

@Composable
fun QuadrantOption(
    title: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color, CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937)
            )
        }
    }
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    var taskTitle by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Add New Task",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text("Task title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color(0xFF6B7280))
                    }

                    Button(
                        onClick = {
                            if (taskTitle.isNotEmpty()) {
                                onAdd(taskTitle)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                        enabled = taskTitle.isNotEmpty()
                    ) {
                        Text("Add Task")
                    }
                }
            }
        }
    }
}

@Composable
fun QuadrantCard(
    title: String,
    subtitle: String,
    color: Color,
    tasks: List<Task>,
    onTaskToggle: (Int) -> Unit,
    onAddTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(color, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F2937)
                    )
                }
            }

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(top = 2.dp, start = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                if (tasks.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No tasks yet",
                            color = Color(0xFF9CA3AF),
                            fontSize = 13.sp
                        )
                    }
                } else {
                    tasks.forEach { task ->
                        TaskItem(
                            task = task,
                            onToggle = { onTaskToggle(task.id) }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = onAddTask,
                    modifier = Modifier.padding(start = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add task",
                        color = Color(0xFF6B7280),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onToggle: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onToggle,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                contentDescription = if (task.isCompleted) "Completed" else "Not completed",
                tint = if (task.isCompleted) Color(0xFF10B981) else Color(0xFFD1D5DB),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = task.title,
            fontSize = 14.sp,
            color = Color(0xFF374151),
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
        )
    }
}