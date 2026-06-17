package com.fajary.focus.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fajary.focus.data.model.ToDoItem
import com.fajary.focus.ui.screen.HomeScreen
import com.fajary.focus.ui.theme.FocusTheme
import com.fajary.focus.viewmodel.AppViewModelMock

@Composable
fun ToDoItemCard(
    item: ToDoItem,
    onCheckboxClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.completed,
                onCheckedChange = { onCheckboxClick() }
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                if (item.description.isNotEmpty()) {
                    Text(text = item.description, style = MaterialTheme.typography.bodyMedium)
                }
                item.deadline?.let {
                    if (it.isNotEmpty()) {
                        Text(text = "Deadline: $it", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            IconButton(onClick = onUpdateClick) {
                Icon(Icons.Default.Edit, contentDescription = "Update")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FocusTheme {
        val viewModel = AppViewModelMock()
        ToDoItemCard(
            item = viewModel.userToDoListItems.value[1],
            onUpdateClick = {

            },
            onDeleteClick = {

            },
            onCheckboxClick = {

            }
        )
    }
}