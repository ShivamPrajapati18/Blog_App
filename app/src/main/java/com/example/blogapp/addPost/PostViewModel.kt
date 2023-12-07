package com.example.blogapp.addPost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) :ViewModel() {
    fun addPost(post: Post){
        viewModelScope.launch(Dispatchers.IO){
            postRepository.addPost(post)
        }
    }
}