package com.example.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.movieapp.models.TitleDetailsResponse
import com.example.movieapp.network.WatchmodeApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val apiService: WatchmodeApiService) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _titleDetails = MutableStateFlow<TitleDetailsResponse?>(null)
    val titleDetails: StateFlow<TitleDetailsResponse?> = _titleDetails

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun fetchTitleDetails(id: Int) {
        _isLoading.value = true
        val disposable = apiService.getTitleDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                _titleDetails.value = response
                _isLoading.value = false

            }, { throwable ->
                // Handle error
                _error.value= "Error fetching details: ${throwable.message}"
                _isLoading.value = false
            })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
