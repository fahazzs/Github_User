package com.dicoding.submissiongithubuser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissiongithubuser.data.response.GithubResponse
import com.dicoding.submissiongithubuser.data.response.ItemsItem
import com.dicoding.submissiongithubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    val _listUser = MutableLiveData<ArrayList<ItemsItem>?>()
    val listUser: MutableLiveData<ArrayList<ItemsItem>?> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun setSearchUser(q: String){
        _isLoading.value = true
        ApiConfig.getApiService()
            .getSearchUsers(q)
            .enqueue(object : Callback<GithubResponse>{
                override fun onResponse(
                    call: Call<GithubResponse>,
                    response: Response<GithubResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _listUser.value = response.body()?.items as ArrayList<ItemsItem>?
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
    }

    fun getListUser(): LiveData<ArrayList<ItemsItem>?> {
        return listUser
    }
}