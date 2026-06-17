package com.fajary.focus.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fajary.focus.ui.theme.FocusTheme
import com.fajary.focus.viewmodel.AppViewModelInterface
import com.fajary.focus.viewmodel.AppViewModelMock
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertToDoItemScreen(vm: AppViewModelInterface) {
    val title by vm.toDoItemTitleInput.collectAsState()
    val description by vm.toDoItemDescriptionInput.collectAsState()
    val deadline by vm.toDoItemDeadlineInput.collectAsState()

    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            vm.onToDoItemDeadlineChange(sdf.format(Date(millis)))
                        }
                        showDatePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { vm.navigateTo("home") }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("New Task", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { vm.onToDoItemTitleChange(it) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { vm.onToDoItemDescriptionChange(it) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp),
            singleLine = false
        )

        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = deadline ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Deadline") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Row {
                    if (!deadline.isNullOrEmpty()) {
                        IconButton(onClick = { vm.onToDoItemDeadlineChange(null) }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { vm.insertToDoItem() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Task")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InsertToDoItemScreenPreview() {
    FocusTheme {
        val viewModel = AppViewModelMock()
        InsertToDoItemScreen(
            vm = viewModel
        )
    }
}