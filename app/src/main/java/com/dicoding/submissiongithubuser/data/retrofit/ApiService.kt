package com.dicoding.submissiongithubuser.data.retrofit

import com.dicoding.submissiongithubuser.data.response.GithubResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") q: String
    ): Call<GithubResponse>
}