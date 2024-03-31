package com.dicoding.submissiongithubuser.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity("favorite_user")
data class MyFavoriteUser(
    @PrimaryKey(autoGenerate = false)
    val login: String = "",
    val avatarUrl: String? = null,
    val id: Int
) : Serializable