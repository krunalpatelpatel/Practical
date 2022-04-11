package com.example.practicaltest.API

import com.example.practicaltest.responseModel.MainListResponse
import com.example.practicaltest.responseModel.SublistResponse
import retrofit2.Response
import retrofit2.http.GET

interface API {

    @GET("index.php?r=configuraciones/franquicias")
    suspend fun callListAPI(
    ): Response<MainListResponse>

    @GET("index.php?r=menu")
    suspend fun callSubListAPI(
    ): Response<SublistResponse>

}