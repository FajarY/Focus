package com.fajary.focus.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.fajary.focus.viewmodel.AppViewModelInterface

@Composable
fun LoginScreen(vm: AppViewModelInterface) {
    val username by vm.loginUsernameInput.collectAsState()
    val password by vm.loginPasswordInput.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { vm.onLoginUsernameChange(it) },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { vm.onLoginPasswordChange(it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { vm.loginUser() }, modifier = Modifier.fillMaxWidth()) {
            Text("Login")
        }
        TextButton(onClick = { vm.changeScreenType() }) {
            Text("Don't have an account? Register")
        }
    }
}