package com.example.popularmovies

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.example.popularmovies.model.Movie
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private val movieAdapter by lazy {
        MovieAdapter(object : MovieAdapter.MovieClickListener {
            override fun onMovieClick(movie: Movie) {
                openMovieDetails(movie)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.movie_list)
        recyclerView.adapter = movieAdapter


        val movieRepository = (application as MovieApplication).movieRepository
        val movieViewModel = ViewModelProvider(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MovieViewModel(movieRepository) as T
            }
        })[MovieViewModel::class.java]

//        movieViewModel.popularMovies.observe(this) { popularMovies ->
//            movieAdapter.addMovies(popularMovies
////                .filter {
////                    it.releaseDate.startsWith(
////                        Calendar.getInstance().get(Calendar.YEAR) .toString()
////                    )
////                }
////                .sortedByDescending { it.popularity }
//            )
//        }
//        movieViewModel.error.observe(this) { error ->
//            if (error.isNotEmpty()) Snackbar.make(recyclerView, error, Snackbar.LENGTH_LONG).show()
//        }

        //14.03
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    movieViewModel.popularMovies.collect { movies ->
                        movieAdapter.addMovies(movies)
                    }
                }
                launch {
                    movieViewModel.error.collect { error ->
                        if (error.isNotEmpty()) Snackbar.make(recyclerView, error, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }



    }

    private fun openMovieDetails(movie: Movie) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_TITLE, movie.title)
            putExtra(DetailsActivity.EXTRA_RELEASE, movie.releaseDate)
            putExtra(DetailsActivity.EXTRA_OVERVIEW, movie.overview)
            putExtra(DetailsActivity.EXTRA_POSTER, movie.posterPath)
        }
        startActivity(intent)
    }
}