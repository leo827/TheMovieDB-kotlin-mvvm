package com.yuzu.themoviedb.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yuzu.themoviedb.R
import com.yuzu.themoviedb.databinding.FragmentMovieDetailBinding
import com.yuzu.themoviedb.model.data.MovieDetail
import com.yuzu.themoviedb.utils.IMG_URL
import com.yuzu.themoviedb.view.activity.MainActivity
import com.yuzu.themoviedb.view.adapter.ReviewAdapter
import com.yuzu.themoviedb.viewmodel.MovieDetailViewModel


class MovieDetailFragment : Fragment() {
    private val LOG_TAG = "MovieDetail"
    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var viewModel: MovieDetailViewModel

    private lateinit var reviewAdapter: ReviewAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        likeOnClick()
        backOnClick()
        onBackPressed()

        initState()

        viewModel.getData(arguments)
        viewModel.movieIdDataLive().observe(viewLifecycleOwner, { viewModel.detail() })
        viewModel.movieDetailDataLive().observe(viewLifecycleOwner, { viewModel.detailRes(this, it) })
        viewModel.movieDetailResDataLive().observe(viewLifecycleOwner, { setImage(it) })
        viewModel.movieDBDataLive().observe(viewLifecycleOwner, { viewModel.movieDBRes(this, binding, it) })
    }

    private fun setImage(data: MovieDetail) {
        Glide.with(this).load(IMG_URL + data.backdropPath).diskCacheStrategy(DiskCacheStrategy.DATA).into(binding.backdrop)
        Glide.with(this).load(IMG_URL + data.posterPath).diskCacheStrategy(DiskCacheStrategy.DATA).into(binding.photo)
        viewModel.genre()
    }


    private fun initState() {
        viewModel.getState().observe(viewLifecycleOwner, { state ->
            viewModel.recyclerViewVisibility(binding, state, reviewAdapter)
            //footerBinding.progressBar.visibility = if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            //footerBinding.txtError.visibility = if (viewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun likeOnClick() {
        binding.like.setOnClickListener {
            viewModel.likeOnClick(binding.like, binding.unlike)
            viewModel.deleteMovieData()
        }

        binding.unlike.setOnClickListener {
            viewModel.likeOnClick(binding.like, binding.unlike)
            viewModel.insertMovieData()
        }
    }

    private fun backOnClick() {
        binding.back.setOnClickListener {
            (activity as MainActivity).replaceFragment(R.id.main_content, MovieFragment(), null)
        }
    }

    private fun onBackPressed() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).replaceFragment(R.id.main_content, MovieFragment(), null)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}