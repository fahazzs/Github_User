package com.dicoding.submissiongithubuser.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissiongithubuser.data.response.DetailUserResponse
import com.dicoding.submissiongithubuser.data.response.ItemsItem
import com.dicoding.submissiongithubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class DetailUserViewModel: ViewModel() {

    val _user = MutableLiveData<DetailUserResponse>()
    val user: MutableLiveData<DetailUserResponse> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "DetailUserViewModel"
    }

    fun setUserDetail(username: String){
        _isLoading.value = true
        ApiConfig.getApiService()
            .getUserDetail(username)
            .enqueue(object : Callback<DetailUserResponse>{
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        val responseBody = response.body()
                        if (responseBody != null){
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

    fun getUserDetail(): LiveData<DetailUserResponse>{
        return user
    }
}