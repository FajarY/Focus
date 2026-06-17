package com.fajary.focus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fajary.focus.data.model.ToDoItem
import com.fajary.focus.data.model.User
import com.fajary.focus.data.repository.ToDoItemRepository
import com.fajary.focus.data.repository.UserRepository
import com.fajary.focus.data.repository.ZenQuotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val userRepository: UserRepository,
    private val toDoItemRepository: ToDoItemRepository,
    private val zenQuotesRepository: ZenQuotesRepository
) : ViewModel(), AppViewModelInterface {

    private val _screenType = MutableStateFlow("login")
    override val screenType: StateFlow<String> = _screenType.asStateFlow()

    private val _isLoadingQuery = MutableStateFlow(false)
    override val isLoadingQuery: StateFlow<Boolean> = _isLoadingQuery.asStateFlow()

    private val _isLoadingQuotes = MutableStateFlow(false)
    override val isLoadingQuotes : StateFlow<Boolean> = _isLoadingQuotes.asStateFlow()

    private val _currentZenQuote = MutableStateFlow<String?>(null)
    override val currentZenQuote: StateFlow<String?> = _currentZenQuote.asStateFlow()

    private val _registerUsernameInput = MutableStateFlow("")
    override val registerUsernameInput: StateFlow<String> = _registerUsernameInput.asStateFlow()

    private val _registerPasswordInput = MutableStateFlow("")
    override val registerPasswordInput: StateFlow<String> = _registerPasswordInput.asStateFlow()

    private val _loginUsernameInput = MutableStateFlow("")
    override val loginUsernameInput: StateFlow<String> = _loginUsernameInput.asStateFlow()

    private val _loginPasswordInput = MutableStateFlow("")
    override val loginPasswordInput: StateFlow<String> = _loginPasswordInput.asStateFlow()

    private val _authenticatedUser = MutableStateFlow<User?>(null)
    override val authenticatedUser: StateFlow<User?> = _authenticatedUser.asStateFlow()

    private val _userToDoListItems = MutableStateFlow<List<ToDoItem>>(emptyList())
    override val userToDoListItems: StateFlow<List<ToDoItem>> = _userToDoListItems.asStateFlow()

    private val _toDoListFilterStatus = MutableStateFlow<String>("all")
    override val toDoListFilterStatus: StateFlow<String> = _toDoListFilterStatus.asStateFlow()

    private val _toDoListSortDeadline = MutableStateFlow<String>("desc")
    override val toDoListSortDeadline: StateFlow<String> = _toDoListSortDeadline.asStateFlow()

    private val _updateUserQuotePreferenceInput = MutableStateFlow("today")
    override val updateUserQuotePreferenceInput: StateFlow<String> = _updateUserQuotePreferenceInput.asStateFlow()

    private val _toDoItemTitleInput = MutableStateFlow("")
    override val toDoItemTitleInput: StateFlow<String> = _toDoItemTitleInput.asStateFlow()

    private val _toDoItemDescriptionInput = MutableStateFlow("")
    override val toDoItemDescriptionInput: StateFlow<String> = _toDoItemDescriptionInput.asStateFlow()

    private val _toDoItemDeadlineInput = MutableStateFlow<String?>(null)
    override val toDoItemDeadlineInput: StateFlow<String?> = _toDoItemDeadlineInput.asStateFlow()

    private val _updatingToDoItemId = MutableStateFlow(0L)
    override val updatingToDoItemId: StateFlow<Long> = _updatingToDoItemId.asStateFlow()

    private val _updateToDoItemTitleInput = MutableStateFlow("")
    override val updateToDoItemTitleInput: StateFlow<String> = _updateToDoItemTitleInput.asStateFlow()

    private val _updateToDoItemDescriptionInput = MutableStateFlow("")
    override val updateToDoItemDescriptionInput: StateFlow<String> = _updateToDoItemDescriptionInput.asStateFlow()

    private val _updateToDoItemDeadlineInput = MutableStateFlow<String?>(null)
    override val updateToDoItemDeadlineInput: StateFlow<String?> = _updateToDoItemDeadlineInput.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    override val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    override fun navigateTo(screen: String) {
        _screenType.value = screen

        if(screen == "home")
        {
            viewModelScope.launch {
                loadQuotes()
            }
        }
    }

    override fun changeScreenType() {
        if(screenType.value == "login")
        {
            navigateTo("register")
        }
        else
        {
            navigateTo("login")
        }
    }

    override fun selectToDoItemForUpdate(id: Long) {
        viewModelScope.launch {
            val item = toDoItemRepository.selectOne(id)
            if (item != null) {
                _updatingToDoItemId.value = item.id
                _updateToDoItemTitleInput.value = item.title
                _updateToDoItemDescriptionInput.value = item.description
                _updateToDoItemDeadlineInput.value = item.deadline
                navigateTo("update")
            }
        }
    }

    override fun registerUser() {
        viewModelScope.launch {
            _isLoadingQuery.value = true
            try {
                val user = userRepository.create(_registerUsernameInput.value, _registerPasswordInput.value)
                if (user != null) {
                    _authenticatedUser.value = user
                    loadUserData()
                    navigateTo("home")
                } else {
                    _errorMessage.value = "Registration failed"
                }
            } catch (e: android.database.sqlite.SQLiteConstraintException) {
                _errorMessage.value = "Username already exists. Please choose another."
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoadingQuery.value = false
            }
        }
    }

    override fun loginUser() {
        viewModelScope.launch {
            _isLoadingQuery.value = true
            try {
                val user = userRepository.login(_loginUsernameInput.value, _loginPasswordInput.value)
                if (user != null) {
                    _authenticatedUser.value = user
                    _updateUserQuotePreferenceInput.value = user.quotePreference
                    loadUserData()
                    navigateTo("home")
                } else {
                    _errorMessage.value = "Invalid credentials"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoadingQuery.value = false
            }
        }
    }

    override fun updateUserQuotePreference() {
        viewModelScope.launch {
            val user = _authenticatedUser.value ?: return@launch
            userRepository.updateQuotePreference(user.id, _updateUserQuotePreferenceInput.value)
            _authenticatedUser.value = userRepository.selectOne(user.id)
            loadUserData()
        }
    }

    override fun deleteCurrentUser() {
        viewModelScope.launch {
            val user = _authenticatedUser.value ?: return@launch
            userRepository.delete(user.id)
            logout()
        }
    }

    override fun logout() {
        _authenticatedUser.value = null
        _userToDoListItems.value = emptyList()
        _currentZenQuote.value = null
        _loginUsernameInput.value = ""
        _loginPasswordInput.value = ""
        navigateTo("login")
    }

    override fun insertToDoItem() {
        viewModelScope.launch {
            val user = _authenticatedUser.value ?: return@launch
            toDoItemRepository.create(
                _toDoItemTitleInput.value,
                _toDoItemDescriptionInput.value,
                _toDoItemDeadlineInput.value ?: "",
                user.id
            )
            _toDoItemTitleInput.value = ""
            _toDoItemDescriptionInput.value = ""
            _toDoItemDeadlineInput.value = null
            loadUserData()
            navigateTo("home")
        }
    }

    override fun updateToDoItem() {
        viewModelScope.launch {
            toDoItemRepository.update(
                _updatingToDoItemId.value,
                _updateToDoItemTitleInput.value,
                _updateToDoItemDescriptionInput.value,
                _updateToDoItemDeadlineInput.value ?: ""
            )
            loadUserData()
            navigateTo("home")
        }
    }

    override fun toggleChecklistToDoItem(id: Long, completed: Boolean)
    {
        _isLoadingQuery.value = true
        viewModelScope.launch {
            try {
                toDoItemRepository.updateCompleted(id, completed)
            }
            catch (e: Exception)
            {

            }
            finally {
                _isLoadingQuery.value = false
            }

            loadUserData()
        }
    }

    override fun deleteToDoItem(id: Long) {
        viewModelScope.launch {
            toDoItemRepository.delete(id)
            loadUserData()
        }
    }

    private suspend fun loadUserData() {
        val user = _authenticatedUser.value ?: return
        _isLoadingQuery.value = true

        try {
            _userToDoListItems.value = toDoItemRepository.selectUserToDoItems(user.id)
        } catch (e: Exception) {
        } finally {
            _isLoadingQuery.value = false
        }
    }

    private suspend fun loadQuotes()
    {
        if(_isLoadingQuotes.value) return

        val user = _authenticatedUser.value ?: return
        _isLoadingQuotes.value = true

        try {
            val quote = if (user.quotePreference == "random") {
                zenQuotesRepository.getRandomQuote()
            } else {
                zenQuotesRepository.getTodayQuote()
            }
            _currentZenQuote.value = "${quote.q} - ${quote.a}"
        } catch (e: Exception) {
            _currentZenQuote.value = "Cannot fetch quotes for now, you are in a good shape already!"
        } finally {
            _isLoadingQuotes.value = false
        }
    }

    // Input Setters
    override fun onRegisterUsernameChange(input: String) { _registerUsernameInput.value = input }
    override fun onRegisterPasswordChange(input: String) { _registerPasswordInput.value = input }
    override fun onLoginUsernameChange(input: String) { _loginUsernameInput.value = input }
    override fun onLoginPasswordChange(input: String) { _loginPasswordInput.value = input }
    override fun onUpdateUserQuotePreferenceChange(input: String) { _updateUserQuotePreferenceInput.value = input }
    override fun onToDoItemTitleChange(input: String) { _toDoItemTitleInput.value = input }
    override fun onToDoItemDescriptionChange(input: String) { _toDoItemDescriptionInput.value = input }
    override fun onToDoItemDeadlineChange(input: String?) { _toDoItemDeadlineInput.value = input }
    override fun onUpdateToDoItemTitleChange(input: String) { _updateToDoItemTitleInput.value = input }
    override fun onUpdateToDoItemDescriptionChange(input: String) { _updateToDoItemDescriptionInput.value = input }
    override fun onUpdateToDoItemDeadlineChange(input: String?) { _updateToDoItemDeadlineInput.value = input }

    override fun onUpdateToDoListFilterStatus(filter: String) { _toDoListFilterStatus.value = filter }
    override fun onUpdateToDoListSortDeadline(sort: String) { _toDoListSortDeadline.value = sort }

    override fun clearError() { _errorMessage.value = null }
}