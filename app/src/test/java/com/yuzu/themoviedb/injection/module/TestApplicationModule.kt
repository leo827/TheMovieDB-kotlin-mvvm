package com.yuzu.themoviedb.injection.module

import com.yuzu.themoviedb.TheMovieDBApplication
import com.yuzu.themoviedb.model.api.MovieAPI
import com.yuzu.themoviedb.model.repository.MovieRepository
import io.mockk.mockk



class TestApplicationModule(application: TheMovieDBApplication): AppModule(application) {
    override fun movieAPI(): MovieAPI = mockk()
    override fun movieRepository(api: MovieAPI): MovieRepository = mockk()
}