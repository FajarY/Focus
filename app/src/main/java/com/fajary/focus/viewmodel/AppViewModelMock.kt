package com.fajary.focus.viewmodel

import com.fajary.focus.data.model.ToDoItem
import com.fajary.focus.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppViewModelMock : AppViewModelInterface {
    override val screenType: StateFlow<String> = MutableStateFlow("home")
    override val isLoadingQuery: StateFlow<Boolean> = MutableStateFlow(false)
    override val isLoadingQuotes: StateFlow<Boolean> = MutableStateFlow(false)
    override val currentZenQuote: StateFlow<String?> = MutableStateFlow("Quote mock - Author")
    override val registerUsernameInput: StateFlow<String> = MutableStateFlow("")
    override val registerPasswordInput: StateFlow<String> = MutableStateFlow("")
    override val loginUsernameInput: StateFlow<String> = MutableStateFlow("")
    override val loginPasswordInput: StateFlow<String> = MutableStateFlow("")
    override val authenticatedUser: StateFlow<User?> = MutableStateFlow(null)
    override val userToDoListItems: StateFlow<List<ToDoItem>> = MutableStateFlow(
        listOf(
            ToDoItem(
                id = 0,
                title = "Do workout",
                description = "Doing push pull legs workout",
                completed = false,
                deadline = "2026-06-03",
                userId = 0
            ),
            ToDoItem(
                id = 0,
                title = "Clean the house",
                description = "Doing push pull legs workout god damn bro we must clean all the shits",
                completed = true,
                deadline = "2026-06-01",
                userId = 0
            )
        )
    )
    override val toDoListFilterStatus: StateFlow<String> = MutableStateFlow("all")
    override val toDoListSortDeadline: StateFlow<String> = MutableStateFlow("desc")
    override val updateUserQuotePreferenceInput: StateFlow<String> = MutableStateFlow("today")
    override val toDoItemTitleInput: StateFlow<String> = MutableStateFlow("")
    override val toDoItemDescriptionInput: StateFlow<String> = MutableStateFlow("")
    override val toDoItemDeadlineInput: StateFlow<String?> = MutableStateFlow(null)
    override val updatingToDoItemId: StateFlow<Long> = MutableStateFlow(0L)
    override val updateToDoItemTitleInput: StateFlow<String> = MutableStateFlow("")
    override val updateToDoItemDescriptionInput: StateFlow<String> = MutableStateFlow("")
    override val updateToDoItemDeadlineInput: StateFlow<String?> = MutableStateFlow(null)
    override val errorMessage: StateFlow<String?> = MutableStateFlow(null)

    override fun navigateTo(screen: String) {}
    override fun changeScreenType() {}
    override fun selectToDoItemForUpdate(id: Long) {}
    override fun registerUser() {}
    override fun loginUser() {}
    override fun updateUserQuotePreference() {}
    override fun deleteCurrentUser() {}
    override fun logout() {}
    override fun insertToDoItem() {}
    override fun updateToDoItem() {}
    override fun toggleChecklistToDoItem(id: Long, completed: Boolean){}
    override fun deleteToDoItem(id: Long) {}
    override fun onUpdateToDoListFilterStatus(filter: String) {}
    override fun onUpdateToDoListSortDeadline(sort: String) {}
    override fun onRegisterUsernameChange(input: String) {}
    override fun onRegisterPasswordChange(input: String) {}
    override fun onLoginUsernameChange(input: String) {}
    override fun onLoginPasswordChange(input: String) {}
    override fun onUpdateUserQuotePreferenceChange(input: String) {}
    override fun onToDoItemTitleChange(input: String) {}
    override fun onToDoItemDescriptionChange(input: String) {}
    override fun onToDoItemDeadlineChange(input: String?) {}
    override fun onUpdateToDoItemTitleChange(input: String) {}
    override fun onUpdateToDoItemDescriptionChange(input: String) {}
    override fun onUpdateToDoItemDeadlineChange(input: String?) {}
    override fun clearError() {}
}