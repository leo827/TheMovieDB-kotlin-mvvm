package com.yuzu.themoviedb.viewmodel

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.yuzu.themoviedb.TheMovieDBApplication
import com.yuzu.themoviedb.databinding.FragmentMovieBinding
import com.yuzu.themoviedb.model.State
import com.yuzu.themoviedb.model.data.MovieData
import com.yuzu.themoviedb.model.datasource.MovieDataSource
import com.yuzu.themoviedb.model.datasource.MovieDataSourceFactory
import com.yuzu.themoviedb.model.repository.MovieDBRepository
import com.yuzu.themoviedb.model.repository.MovieRepository
import com.yuzu.themoviedb.view.adapter.MovieAdapter
import io.reactivex.disposables.CompositeDisposable


class MovieViewModel: ViewModel() {
    private val LOG_TAG = "Movie"
    var loading: MutableLiveData<Boolean> = MutableLiveData(false)

    private val compositeDisposable = CompositeDisposable()
    private val movieRepository: MovieRepository
    private val movieDBRepository: MovieDBRepository
    private var movieDataSourceFactory: MovieDataSourceFactory? = null

    private val movieData = MutableLiveData<Int>()
    fun movieDataLive(): LiveData<Int> = movieData

    var moviePagedList: LiveData<PagedList<MovieData>>
    var type = MutableLiveData<String>()
    private val pageSize = 5

    init {
        val appComponent = TheMovieDBApplication.instance.getAppComponent()
        movieRepository = appComponent.movieRepository()
        movieDBRepository = appComponent.movieDBRepository()

        movieDataSourceFactory = MovieDataSourceFactory(movieRepository, movieDBRepository, compositeDisposable, "")
        val config = PagedList.Config.Builder().setPageSize(pageSize).setInitialLoadSizeHint(pageSize).setEnablePlaceholders(false).build()
        //moviePagedList = LivePagedListBuilder(movieDataSourceFactory, config).build()

        moviePagedList = Transformations.switchMap(type) { input ->
            return@switchMap if (input == null || input.equals("") || input.equals("%%")) {
                //check if the current value is empty load all data else search
                LivePagedListBuilder(movieDataSourceFactory!!, config).build()

            } else {
                movieDataSourceFactory = MovieDataSourceFactory(movieRepository, movieDBRepository, compositeDisposable, input)
                LivePagedListBuilder(movieDataSourceFactory!!, config).build()
            }
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun checkPrevFragment(prev: Fragment?, fragmentTransaction: FragmentTransaction) {
        if (prev != null) {
            fragmentTransaction.remove(prev)
        }
        fragmentTransaction.addToBackStack(null)
    }

    fun retry() {
        movieDataSourceFactory!!.movieDataSourceLiveData.value?.retry()
    }

    fun getState(): LiveData<State> = Transformations.switchMap(
        movieDataSourceFactory!!.movieDataSourceLiveData,
        MovieDataSource::state
    )

    private fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    fun recyclerViewVisibility(binding: FragmentMovieBinding, state: State, movieAdapter: MovieAdapter) {
        if (listIsEmpty() && state == State.LOADING) {
            loading.value = true
            binding.recyclerView.visibility = View.GONE
            //binding.txtError.visibility = View.GONE

        } else if (listIsEmpty() && state == State.ERROR) {
            loading.value = false
            //binding.txtError.visibility = View.VISIBLE

        } else {
            loading.value = false
            binding.recyclerView.visibility = View.VISIBLE
            //binding.txtError.visibility = View.GONE
        }

        if (!listIsEmpty()) {
            movieAdapter.setState(state)
        }
    }

    fun itemOnClick(id: Int?) {
        if (id != null) {
            loading.value = true
            movieData.value = id
        }
    }

    fun likeOnClick(like: ImageView, unlike: ImageView) {
        if (unlike.visibility == View.VISIBLE) {
            unlike.visibility = View.GONE
        } else {
            unlike.visibility = View.VISIBLE
        }

        if (like.visibility == View.VISIBLE) {
            like.visibility = View.GONE
        } else {
            like.visibility = View.VISIBLE
        }
    }
}