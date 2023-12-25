package com.example.blogapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id:String,
    val name:String,
    val email:String
):Parcelable