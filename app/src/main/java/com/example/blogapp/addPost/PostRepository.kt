package com.example.blogapp.addPost

import com.example.blogapp.di.PostNode
import com.example.blogapp.model.Post
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostRepository @Inject constructor(){
    @Inject
    @PostNode
    lateinit var postRef:CollectionReference

    suspend fun addPost(post: Post)= withContext(Dispatchers.IO){
        postRef.add(post)
    }
}