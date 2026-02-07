package com.example.add_friends

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.compose.ui.Alignment
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.add_friends.ui.theme.AddFriendsTheme

// Helper to fetch contacts
fun getContacts(context: Context): List<String> {
    val contacts = mutableListOf<String>()
    val cursor = context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME),
        null, null, null
    )
    cursor?.use {
        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        while (it.moveToNext()) {
            val name = it.getString(nameIndex)
            contacts.add(name)
        }
    }
    return contacts
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request runtime permission for contacts
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 1)
        }

        setContent {
            AddFriendsTheme {
                var selectedItem by remember { mutableStateOf("Home") }

                Scaffold(
                    bottomBar = {
                        BottomNavBar(
                            selectedItem = selectedItem,
                            onItemSelected = { selectedItem = it }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        when (selectedItem) {
                            "Home" -> HomeScreen()
                            "To-Do" -> TodoScreen()
                            "Analytics" -> AnalyticsScreen()
                            "Friends" -> FriendsScreen()
                            "Resources" -> ResourcesScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(selectedItem: String, onItemSelected: (String) -> Unit) {
    val items = listOf("Home", "To-Do", "Analytics", "Friends", "Resources")

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    when (item) {
                        "Home" -> Icon(Icons.Default.Home, contentDescription = "Home")
                        "To-Do" -> Icon(Icons.Default.List, contentDescription = "To-Do")
                        "Analytics" -> Icon(Icons.Filled.BarChart, contentDescription = "Analytics")
                        "Friends" -> Icon(Icons.Default.People, contentDescription = "Friends")
                        "Resources" -> Icon(Icons.Default.Book, contentDescription = "Resources")
                    }
                },
                label = { Text(item) },
                selected = selectedItem == item,
                onClick = { onItemSelected(item) }
            )
        }
    }
}

@Composable
fun HomeScreen() {
    Text("Home Screen", modifier = Modifier.padding(16.dp))
}

@Composable
fun TodoScreen() {
    Text("To-Do Screen", modifier = Modifier.padding(16.dp))
}

@Composable
fun AnalyticsScreen() {
    Text("Analytics Screen", modifier = Modifier.padding(16.dp))
}

@Composable
fun FriendsScreen(context: Context = LocalContext.current) {
    var suggestedFriends by remember { mutableStateOf(getContacts(context)) }
    var myFriends by remember { mutableStateOf(listOf<String>()) }
    var selectedTab by remember { mutableStateOf(0) } // 0 = Suggested, 1 = My Friends

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("SUGGESTED") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("MY FRIENDS") }
            )
        }

        when (selectedTab) {
            0 -> LazyColumn {
                items(suggestedFriends) { name ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(name.first().toString(), color = Color.White)
                        }
                        Text(name, modifier = Modifier.weight(1f).padding(start = 8.dp))
                        Button(onClick = {
                            myFriends = myFriends + name
                            suggestedFriends = suggestedFriends - name
                        }) {
                            Text("ADD")
                        }
                    }
                }
            }
            1 -> LazyColumn {
                items(myFriends) { friend ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.secondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(friend.first().toString(), color = Color.White)
                        }
                        Text(friend, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ResourcesScreen() {
    Text("Resources Screen", modifier = Modifier.padding(16.dp))
}