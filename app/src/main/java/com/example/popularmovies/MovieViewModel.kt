package com.example.popularmovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.popularmovies.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    init {
        fetchPopularMovies()
    }
    val popularMovies: LiveData<List<Movie>>
    //I modified to last exercise filter to see the output
        get() = movieRepository.movies.map { list ->
            list
                            .filter {
                    it.releaseDate.startsWith(
                        Calendar.getInstance().get(Calendar.YEAR) .toString()
                    )
                }
                .sortedByDescending { it.popularity }

            //last month popular movies were not available
//                .filter {
//                val cal = Calendar.getInstance()
//                cal.add(Calendar.MONTH, -1)
//                it.releaseDate.startsWith(
//                    "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 4}"
//                )
//            }.sortedByDescending { it.popularity }
        }

    val error: LiveData<String> = movieRepository.error

    private fun fetchPopularMovies() {
        viewModelScope.launch(Dispatchers.IO)  {
            movieRepository.fetchMovies()
        }
    }


}