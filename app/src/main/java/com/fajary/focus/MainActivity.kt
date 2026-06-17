package com.fajary.focus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fajary.focus.data.AppDatabaseProvider
import com.fajary.focus.data.repository.ToDoItemRepository
import com.fajary.focus.data.repository.UserRepository
import com.fajary.focus.data.repository.ZenQuotesRepository
import com.fajary.focus.ui.screen.HomeScreen
import com.fajary.focus.ui.screen.InsertToDoItemScreen
import com.fajary.focus.ui.screen.LoginScreen
import com.fajary.focus.ui.screen.RegisterScreen
import com.fajary.focus.ui.screen.UpdateToDoItemScreen
import com.fajary.focus.ui.screen.UserInformationScreen
import com.fajary.focus.ui.theme.FocusTheme
import com.fajary.focus.viewmodel.AppViewModel
import com.fajary.focus.viewmodel.AppViewModelInterface
import com.fajary.focus.viewmodel.AppViewModelMock

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FocusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    val database = remember {
                        AppDatabaseProvider.getDatabase(context)
                    }
                    val userRepository = remember {
                        UserRepository(database.userDao())
                    }
                    val toDoItemRepository = remember {
                        ToDoItemRepository(database.toDoItemDao())
                    }
                    val zenQuotesRepository = remember {
                        ZenQuotesRepository()
                    }
                    val viewModel = remember {
                        AppViewModel(userRepository, toDoItemRepository, zenQuotesRepository)
                    }

                    FocusApp(
                        modifier = Modifier.padding(innerPadding),
                        vm = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun FocusApp(modifier: Modifier, vm: AppViewModelInterface) {
    val screenType by vm.screenType.collectAsState()
    val user by vm.authenticatedUser.collectAsState()
    val error by vm.errorMessage.collectAsState()
    val isLoading by vm.isLoadingQuery.collectAsState()

    Box(modifier = modifier.fillMaxSize().padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)) {
        when {
            user == null && screenType == "register" -> RegisterScreen(vm)
            user == null && screenType == "login" -> LoginScreen(vm)
            screenType == "insert" -> InsertToDoItemScreen(vm)
            screenType == "update" -> UpdateToDoItemScreen(vm)
            screenType == "info" -> UserInformationScreen(vm)
            else -> HomeScreen(vm)
        }

        error?.let {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { vm.clearError() },
                confirmButton = {
                    TextButton(onClick = { vm.clearError() }) {
                        Text("OK")
                    }
                },
                title = { Text("Error") },
                text = { Text(it) }
            )
        }
    }

    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f))
        )
        {
            CircularProgressIndicator(
                modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FocusTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val viewModel = AppViewModelMock()
            FocusApp(
                modifier = Modifier.padding(innerPadding),
                vm = viewModel
            )
        }
    }
}