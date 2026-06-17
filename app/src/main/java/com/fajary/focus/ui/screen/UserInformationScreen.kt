package com.fajary.focus.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fajary.focus.viewmodel.AppViewModelInterface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInformationScreen(vm: AppViewModelInterface) {
    val user by vm.authenticatedUser.collectAsState()
    val prefInput by vm.updateUserQuotePreferenceInput.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("today", "random")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Info") },
                navigationIcon = {
                    IconButton(onClick = { vm.navigateTo("home") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Username: ${user?.username}", style = MaterialTheme.typography.titleMedium)
                }
            }

            HorizontalDivider()

            Text("Change Quote Preference", style = MaterialTheme.typography.titleLarge)

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = if(prefInput == "today")
                        "Today"
                    else
                        "Random",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Preference") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(
                                if(selectionOption == "today")
                                    "Today"
                                else
                                    "Random"
                            ) },
                            onClick = {
                                vm.onUpdateUserQuotePreferenceChange(selectionOption)
                                expanded = false
                                vm.updateUserQuotePreference()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { vm.logout() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Logout")
            }
            OutlinedButton(
                onClick = { vm.deleteCurrentUser() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Account")
            }
        }
    }
}