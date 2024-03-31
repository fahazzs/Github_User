package com.dicoding.submissiongithubuser.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.submissiongithubuser.data.local.MyFavoriteUser
import com.dicoding.submissiongithubuser.data.local.MyFavoriteUserDao
import com.dicoding.submissiongithubuser.data.local.UserDatabase
import com.dicoding.submissiongithubuser.data.response.DetailUserResponse
import com.dicoding.submissiongithubuser.data.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {

    val _user = MutableLiveData<DetailUserResponse>()
    val user: MutableLiveData<DetailUserResponse> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var userDao: MyFavoriteUserDao?
    private var userDB: UserDatabase?

    init {
        userDB = UserDatabase.getDatabase(application)
        userDao = userDB?.myFavoriteUserDao()
    }

    fun setUserDetail(username: String) {
        _isLoading.value = true
        ApiConfig.getApiService()
            .getUserDetail(username)
            .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _user.postValue(response.body())
                        } else {
                            Log.e(TAG, "onFailure: ${response.message()}")
                        }
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
    }

    fun getUserDetail(): LiveData<DetailUserResponse> {
        return user
    }

    fun addToFavorite(id: Int, username: String, avatarUrl: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = MyFavoriteUser(username, avatarUrl, id)
            userDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeUser(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeUser(id)
        }
    }

    companion object {
        private const val TAG = "DetailUserViewModel"
    }
}