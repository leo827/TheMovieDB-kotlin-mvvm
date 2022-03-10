package com.yuzu.themoviedb.model.api

import com.yuzu.themoviedb.model.data.Movie
import com.yuzu.themoviedb.model.data.MovieDetail
import com.yuzu.themoviedb.model.data.Review
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieAPI {

    @GET(value = "movie/popular")
    fun popular(@Query("page") page: Int, @Query("api_key") apiKey: String): Single<Movie>

    @GET(value = "movie/top_rated")
    fun topRated(@Query("page") page: Int, @Query("api_key") apiKey: String): Single<Movie>

    @GET(value = "movie/now_playing")
    fun nowPlaying(@Query("page") page: Int, @Query("api_key") apiKey: String): Single<Movie>


    @GET(value = "movie/{id}")
    fun details(@Path("id") id: Int, @Query("api_key") apiKey: String): Single<MovieDetail>

    @GET(value = "movie/{id}/reviews")
    fun reviews(@Path("id") id: Int, @Query("page") page: Int, @Query("api_key") apiKey: String): Single<Review>
}