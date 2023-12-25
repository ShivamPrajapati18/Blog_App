package com.example.blogapp.login

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject
constructor(private val usersRepository: UsersRepository) : ViewModel() {
    fun addUSerinDb(user: User) {
        viewModelScope.launch {
            usersRepository.addUserInDB(user)
        }
    }

    fun addProfileImage(name:String,profileImage: Uri){
        viewModelScope.launch {
            usersRepository.addProfileImage(name,profileImage)
        }
    }
}