package com.yuzu.themoviedb.model.repository

import com.yuzu.themoviedb.model.data.MovieData
import com.yuzu.themoviedb.model.local.MovieDAO
import io.reactivex.Single
import java.util.concurrent.Executor


class MovieDBRepositoryImpl(private val dao: MovieDAO, private val exec: Executor): MovieDBRepository {
    override fun getAll(): Single<List<MovieData>> {
        return dao.getAll()
    }

    override fun getDataById(id: Int): Single<MovieData> {
        return dao.getDataById(id)
    }

    override fun insert(data: MovieData) {
        exec.execute {
            dao.insert(data)
        }
    }

    override fun insert(data: List<MovieData>) {
        exec.execute {
            dao.insert(data)
        }
    }

    override fun delete(data: MovieData) {
        exec.execute {
            dao.delete(data)
        }
    }
}