package com.example.popularmovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.popularmovies.api.MovieService
import com.example.popularmovies.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(private val movieService: MovieService) {

    private val apiKey = "4b6a464636483bd0c7d5451186b74c0e"

//    private val movieLiveData = MutableLiveData<List<Movie>>()
//    private val errorLiveData = MutableLiveData<String>()
//    val movies: LiveData<List<Movie>>
//        get() = movieLiveData
//    val error: LiveData<String>
//        get() = errorLiveData
//
//
//    suspend fun fetchMovies() {
//        try {
//            val popularMovies = movieService.getPopularMovies(apiKey)
//            movieLiveData.postValue(popularMovies.results)
//        } catch (exception: Exception) {
//            errorLiveData.postValue("An error occurred: ${exception.message}")
//        }
//    }

    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            emit(movieService.getPopularMovies(apiKey).results)
        }.flowOn(Dispatchers.IO)
    }


}