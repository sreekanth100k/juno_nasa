package com.juno.nasa

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("apod?api_key=DEMO_KEY")
    fun fetchApiResponse():Call<TestResponse>

    @GET("apod?api_key=DEMO_KEY")
    fun fetchApiResponseForDate(@Query("date") date:String):Call<TestResponse>

}