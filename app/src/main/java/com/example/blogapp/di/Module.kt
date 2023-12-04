package com.example.blogapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Provides
    @Singleton
    @UserNode
    fun provideFireStoreUserNode(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("user")
    }
    @Provides
    @Singleton
    @PostNode
    fun provideFireStorePostNode(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("post")
    }
}