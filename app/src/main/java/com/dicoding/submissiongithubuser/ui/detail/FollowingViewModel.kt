package com.dicoding.submissiongithubuser.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissiongithubuser.data.response.ItemsItem
import com.dicoding.submissiongithubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    val _listFollowing = MutableLiveData<ArrayList<ItemsItem>?>()
    val listFollowing: MutableLiveData<ArrayList<ItemsItem>?> = _listFollowing

    companion object {
        private const val TAG = "FollowingViewModel"
    }

    fun setListFollowing(username: String) {
        ApiConfig.getApiService()
            .getFollowing(username)
            .enqueue(object : Callback<ArrayList<ItemsItem>?> {
                override fun onResponse(
                    call: Call<ArrayList<ItemsItem>?>,
                    response: Response<ArrayList<ItemsItem>?>
                ) {
                    if (response.isSuccessful) {
                        _listFollowing.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<ItemsItem>?>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
    }

    fun getListFollowing(): LiveData<ArrayList<ItemsItem>?> {
        return listFollowing
    }
}