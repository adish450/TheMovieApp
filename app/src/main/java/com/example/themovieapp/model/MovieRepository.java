package com.example.themovieapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.themovieapp.R;
import com.example.themovieapp.serviceapi.MovieApiService;
import com.example.themovieapp.serviceapi.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private final Application application;
    private ArrayList<Movie> movies = new ArrayList<>();
    private final MutableLiveData<List<Movie>> popularMovies = new MutableLiveData<List<Movie>>();

    public MovieRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Movie>> getPopularMovies() {
        MovieApiService retrofitInstance = RetrofitInstance.getService();
        Call<Result> call = retrofitInstance.getPopularMovies(application.getApplicationContext().getString(R.string.api_key));
        call.enqueue( // Asynchronous method call
                new Callback<Result>() {
                    @Override
                    public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                        // Success
                        if (response.body() != null && response.body().getResults() != null) {
                            Result result = response.body();

                            if (result != null && result.getResults() != null) {
                                movies = (ArrayList<Movie>) result.getResults();
                                popularMovies.setValue(movies); // called on main UI thread
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Result> call, @NonNull Throwable throwable) {

                    }
                }
        );
        return popularMovies;
    }
}
