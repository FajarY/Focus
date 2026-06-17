package com.fajary.focus.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fajary.focus.FocusApp
import com.fajary.focus.ui.component.ToDoItemCard
import com.fajary.focus.ui.theme.FocusTheme
import com.fajary.focus.viewmodel.AppViewModelInterface
import com.fajary.focus.viewmodel.AppViewModelMock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: AppViewModelInterface) {
    val quote by vm.currentZenQuote.collectAsState()
    val todos by vm.userToDoListItems.collectAsState()
    val user by vm.authenticatedUser.collectAsState()

    val filterStatus by vm.toDoListFilterStatus.collectAsState()
    val sortDeadline by vm.toDoListSortDeadline.collectAsState()

    val displayList by remember(todos, filterStatus, sortDeadline) {
        derivedStateOf {
            var list = todos
            list = when (filterStatus) {
                "completed" -> list.filter { it.completed }
                "incomplete" -> list.filter { !it.completed }
                else -> list
            }
            list = if (sortDeadline == "asc") {
                list.sortedWith(compareBy(nullsLast()) { it.deadline })
            } else {
                list.sortedWith(compareBy(nullsLast()) { it.deadline }).reversed()
            }
            list
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                "Welcome, ${user?.username}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { vm.navigateTo("info") }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
            IconButton(onClick = { vm.logout() }) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                text = quote ?: "Loading quote...",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            FilterChip(
                modifier = Modifier.weight(1f),
                selected = filterStatus == "all",
                onClick = { vm.onUpdateToDoListFilterStatus("all") },
                label = { Text("All", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                shape = RoundedCornerShape(
                    topStart = 12.dp,
                    bottomStart = 12.dp,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp
                )
            )
            FilterChip(
                modifier = Modifier.weight(1f),
                selected = filterStatus == "completed",
                onClick = { vm.onUpdateToDoListFilterStatus("completed") },
                label = { Text("Completed", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                shape = RectangleShape
            )
            FilterChip(
                modifier = Modifier.weight(1f),
                selected = filterStatus == "incomplete",
                onClick = { vm.onUpdateToDoListFilterStatus("incomplete") },
                label = { Text("Incomplete", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    bottomStart = 0.dp,
                    topEnd = 12.dp,
                    bottomEnd = 12.dp
                )

            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Button (
                onClick = {
                    if(sortDeadline == "asc")
                    {
                        vm.onUpdateToDoListSortDeadline("desc")
                    }
                    else
                    {
                        vm.onUpdateToDoListSortDeadline("asc")
                    }
                },
                shape = RoundedCornerShape(
                    topStart = 12.dp,
                    bottomStart = 12.dp,
                    topEnd = 12.dp,
                    bottomEnd = 12.dp
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
            )
            {
                Text(
                    text = "Sort ",
                    modifier = Modifier.padding(start = 4.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Sort Button",
                    modifier = Modifier.size(20.dp).rotate(
                        if (sortDeadline == "asc")
                            90f
                        else
                            -90f
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (displayList.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("No tasks match your filters.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(displayList) { item ->
                    ToDoItemCard(
                        item = item,
                        onUpdateClick = { vm.selectToDoItemForUpdate(item.id) },
                        onDeleteClick = { vm.deleteToDoItem(item.id) },
                        onCheckboxClick = {
                            vm.toggleChecklistToDoItem(item.id,
                                if(item.completed)
                                    false
                                else
                                    true)
                        }
                    )
                }
            }
        }

        Button(
            onClick = { vm.navigateTo("insert") },
            modifier = Modifier.align(Alignment.End),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FocusTheme {
        val viewModel = AppViewModelMock()
        HomeScreen(
            vm = viewModel
        )
    }
}