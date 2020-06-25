package com.juno.nasa

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("apod?api_key=DEMO_KEY")
    fun fetchApiResponse():Call<TestResponse>

    @GET("apod?api_key=DEMO_KEY")
    fun fetchApiResponseForDate(date:String):Call<TestResponse>

}