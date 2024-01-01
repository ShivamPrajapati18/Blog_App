package com.example.blogapp.login

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.blogapp.di.UserNode
import com.example.blogapp.model.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRepository @Inject constructor(){
    @Inject
    @UserNode
    lateinit var usersRef:CollectionReference
    private var _user=MutableStateFlow("")
    val user:StateFlow<String>
        get() = _user

    @Inject
    lateinit var storage:StorageReference
    private var _profileImg= MutableLiveData<Uri>()
    val profileImg:LiveData<Uri>
        get() = _profileImg
    suspend fun addUserInDB(user: User) = withContext(Dispatchers.IO) {
        usersRef.add(user)
    }

    suspend fun getUser(authId:String) {
        val document= usersRef.whereEqualTo("id",authId).get().await()
        document.forEach {
            val name = it.getString("name")
            name?.let {
                _user.value=it
            }
        }
    }

    suspend fun addProfileImage(name:String,uri: Uri)= withContext(Dispatchers.IO){
        storage.child("profile/$name").putFile(uri)
    }

    suspend fun getProfileImage(name: String){
        val img=storage.child("profile/$name").downloadUrl.await()
        _profileImg.value=img
    }
}
