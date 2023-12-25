package com.example.blogapp.addPost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.login.UsersRepository
import com.example.blogapp.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val usersRepository: UsersRepository,
) : ViewModel() {
    val allPost
        get() = postRepository.postList

    val user = usersRepository.user
    val profileImg
            =usersRepository.profileImg
    fun addPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.addPost(post)
        }
    }

    fun getPost() {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.getPost()
        }
    }

    fun getUser(authId: String) {
        viewModelScope.launch {
            usersRepository.getUser(authId)
        }
    }

    fun getProfileImage(name:String){
        viewModelScope.launch {
            usersRepository.getProfileImage(name)
        }
    }
}