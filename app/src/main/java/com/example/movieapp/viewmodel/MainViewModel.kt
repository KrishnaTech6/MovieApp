package com.example.movieapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.movieapp.models.Title
import com.example.movieapp.network.WatchmodeApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val apiService: WatchmodeApiService) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var currentPage = 1

    // Use separate states for Movies and TV Shows
    private val _titles = MutableStateFlow<List<Title>>(emptyList())
    val titles: StateFlow<List<Title>> = _titles

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isShowingMovies = MutableStateFlow(true)
    val isShowingMovies: StateFlow<Boolean> = _isShowingMovies

    init {
        fetchTitles()
    }

    fun setShowingMovies(isMovies: Boolean) {
        _isShowingMovies.value = isMovies
    }

    // Fetch movies and tv shows simultaneously
     fun fetchTitles() {
        Log.d("Tag", "fetchTitles: callled 1" )
        _isLoading.value = true
        //Single.zip() is not useful as we have to filter the returned list of titles wrt imdb_type=="tv" or "movie"
        val disposable = apiService.getTitles(page = currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _titles.value = result.titles
                _isLoading.value = false
                currentPage++
            }, { throwable ->
                _error.value = "Error fetching details: ${throwable.message}"
                _isLoading.value = false
            })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
