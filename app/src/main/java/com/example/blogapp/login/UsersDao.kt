package com.example.blogapp.login

import com.example.blogapp.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersDao @Inject constructor(){
    private val usersRef=FirebaseFirestore.getInstance().collection("users")
    suspend fun addUserInDB(user: User) = withContext(Dispatchers.IO) {
        usersRef.add(user)
    }
}