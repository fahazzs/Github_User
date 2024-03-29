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

class FollowersViewModel: ViewModel() {

    val _listFollowers = MutableLiveData<ArrayList<ItemsItem>?>()
    val listFollowers: MutableLiveData<ArrayList<ItemsItem>?> = _listFollowers

    companion object {
        private const val TAG = "FollowersViewModel"
    }

    fun setListFollowers(username: String){
        ApiConfig.getApiService()
            .getFollowers(username)
            .enqueue(object : Callback<ArrayList<ItemsItem>?>{
                override fun onResponse(
                    call: Call<ArrayList<ItemsItem>?>,
                    response: Response<ArrayList<ItemsItem>?>
                ) {
                    if (response.isSuccessful){
                        _listFollowers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<ItemsItem>?>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }

            })
    }

    fun getListFollowers(): LiveData<ArrayList<ItemsItem>?>{
        return listFollowers
    }
}