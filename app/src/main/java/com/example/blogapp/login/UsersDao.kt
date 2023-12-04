package com.example.blogapp.login

import com.example.blogapp.di.PostNode
import com.example.blogapp.di.UserNode
import com.example.blogapp.model.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersDao @Inject constructor(){
    @Inject
    @UserNode
    lateinit var usersRef:CollectionReference

    @Inject
    @PostNode
    lateinit var postRef:CollectionReference
    suspend fun addUserInDB(user: User) = withContext(Dispatchers.IO) {
        usersRef.add(user)

        postRef.add(user)
    }
}
