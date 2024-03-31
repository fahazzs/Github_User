package com.dicoding.submissiongithubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.submissiongithubuser.data.local.MyFavoriteUser
import com.dicoding.submissiongithubuser.data.local.MyFavoriteUserDao
import com.dicoding.submissiongithubuser.data.local.UserDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var userDao: MyFavoriteUserDao?
    private var userDB: UserDatabase?

    init {
        userDB = UserDatabase.getDatabase(application)
        userDao = userDB?.myFavoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<MyFavoriteUser>>? {
        return userDao?.getFavorite()
    }
}