package com.fajary.focus.viewmodel

import com.fajary.focus.data.model.ToDoItem
import com.fajary.focus.data.model.User
import kotlinx.coroutines.flow.StateFlow

interface AppViewModelInterface {
    val screenType: StateFlow<String>
    val isLoadingQuery: StateFlow<Boolean>
    val isLoadingQuotes: StateFlow<Boolean>

    val currentZenQuote: StateFlow<String?>

    val registerUsernameInput: StateFlow<String>
    val registerPasswordInput: StateFlow<String>
    val loginUsernameInput: StateFlow<String>
    val loginPasswordInput: StateFlow<String>

    val authenticatedUser: StateFlow<User?>
    val userToDoListItems: StateFlow<List<ToDoItem>>
    val toDoListFilterStatus: StateFlow<String>
    val toDoListSortDeadline: StateFlow<String>

    val updateUserQuotePreferenceInput: StateFlow<String>

    val toDoItemTitleInput: StateFlow<String>
    val toDoItemDescriptionInput: StateFlow<String>
    val toDoItemDeadlineInput: StateFlow<String?>

    val updatingToDoItemId: StateFlow<Long>
    val updateToDoItemTitleInput: StateFlow<String>
    val updateToDoItemDescriptionInput: StateFlow<String>
    val updateToDoItemDeadlineInput: StateFlow<String?>

    val errorMessage: StateFlow<String?>

    // Navigation
    fun navigateTo(screen: String)
    fun changeScreenType()
    fun selectToDoItemForUpdate(id: Long)

    // Auth
    fun registerUser()
    fun loginUser()
    fun updateUserQuotePreference()
    fun deleteCurrentUser()
    fun logout()

    // To-Do
    fun insertToDoItem()
    fun updateToDoItem()
    fun toggleChecklistToDoItem(id: Long, completed: Boolean)
    fun deleteToDoItem(id: Long)
    fun onUpdateToDoListFilterStatus(filter: String)
    fun onUpdateToDoListSortDeadline(sort: String)

    // Input Setters
    fun onRegisterUsernameChange(input: String)
    fun onRegisterPasswordChange(input: String)
    fun onLoginUsernameChange(input: String)
    fun onLoginPasswordChange(input: String)
    fun onUpdateUserQuotePreferenceChange(input: String)
    fun onToDoItemTitleChange(input: String)
    fun onToDoItemDescriptionChange(input: String)
    fun onToDoItemDeadlineChange(input: String?)
    fun onUpdateToDoItemTitleChange(input: String)
    fun onUpdateToDoItemDescriptionChange(input: String)
    fun onUpdateToDoItemDeadlineChange(input: String?)

    fun clearError()
}