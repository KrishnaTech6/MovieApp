package com.example.movieapp.network

import com.example.movieapp.BuildConfig
import com.example.movieapp.models.TitleDetailsResponse
import com.example.movieapp.models.TitlesResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WatchmodeApiService {
    @GET("list-titles/")
    fun getTitles(
        @Query("apiKey") apiKey: String= BuildConfig.API_KEY,
        @Query("source_ids") sourceIds: String="203,57",
        @Query("page") page: Int = 1
    ): Single<TitlesResponse>

    @GET("title/{id}/details/")
    fun getTitleDetails(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String= BuildConfig.API_KEY
    ): Single<TitleDetailsResponse>
}