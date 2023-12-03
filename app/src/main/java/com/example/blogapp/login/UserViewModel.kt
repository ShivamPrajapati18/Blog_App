package com.example.blogapp.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject
constructor(val usersDao: UsersDao) : ViewModel() {

    fun addUSerinDb(user: User) {
        viewModelScope.launch {
            usersDao.addUserInDB(user)
        }
    }
}