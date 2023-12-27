package com.example.blogapp.addPost

import android.util.Log
import com.example.blogapp.di.PostNode
import com.example.blogapp.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostRepository @Inject constructor(){
    @Inject
    @PostNode
    lateinit var postRef:CollectionReference
    private val _postList= MutableStateFlow<List<Post>>(emptyList())
    val postList:StateFlow<List<Post>>
        get() = _postList.asStateFlow()

    private val _userPostList= MutableStateFlow<List<Post>>(emptyList())
    val userPostList:StateFlow<List<Post>>
        get() = _userPostList


    suspend fun addPost(post: Post)= withContext(Dispatchers.IO){
        postRef.add(post)
    }

    suspend fun getPost(uid: String){
        val tempPostList= mutableListOf<Post>()
        val allPost=postRef.whereNotEqualTo("authorId",uid)
            .get().await()
        allPost.forEach {
            if (it.exists()){
                val post=it.toObject<Post>()
                tempPostList.add(post)
                Log.d("tag", post.toString())
            }
        }
        _postList.value=tempPostList
    }

    suspend fun getUserPost(uid:String){
        val tempPostList= mutableListOf<Post>()
        val allPost=postRef.whereEqualTo("authorId",uid)
            .get().await()
        allPost.forEach {
            if (it.exists()){
                val post=it.toObject<Post>()
                tempPostList.add(post)
                Log.d("tag", post.toString())
            }
        }
        _userPostList.value=tempPostList
    }
}