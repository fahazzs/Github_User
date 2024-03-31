package com.dicoding.submissiongithubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MyFavoriteUserDao {
    @Insert
    suspend fun addToFavorite(myFav: MyFavoriteUser)

    @Query("SELECT * FROM favorite_user")
    fun getFavorite(): LiveData<List<MyFavoriteUser>>

    @Query("SELECT count(*) FROM favorite_user WHERE id= :id")
    suspend fun checkUser(id: Int): Int

    @Query("DELETE FROM favorite_user WHERE id= :id")
    suspend fun removeUser(id: Int): Int
}