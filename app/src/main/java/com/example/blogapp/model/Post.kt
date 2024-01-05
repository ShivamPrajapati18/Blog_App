package com.example.blogapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val postId:String="",
    val authorId: String = "",
    val authorName:String="",
    val title: String = "",
    val description: String = "",
    val date:String="",
    val likedBy: ArrayList<String> =ArrayList()
):Parcelable
