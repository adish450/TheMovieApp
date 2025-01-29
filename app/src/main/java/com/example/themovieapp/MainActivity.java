package com.example.themovieapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.themovieapp.databinding.ActivityMainBinding;
import com.example.themovieapp.model.Movie;
import com.example.themovieapp.view.MovieAdapter;
import com.example.themovieapp.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    MovieAdapter movieAdapter;
    MainActivityViewModel viewModel;
    ArrayList<Movie> movies;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_main);

        viewModel = new ViewModelProvider(this)
                .get(MainActivityViewModel.class);

        getPopularMovies();

        swipeRefreshLayout = binding.swipeRefreshLayout;
        swipeRefreshLayout.setColorSchemeResources(R.color.black);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPopularMovies();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void getPopularMovies() {
        viewModel.getPopularMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> updatedMovies) {
                movies = (ArrayList<Movie>) updatedMovies;
                displayMoviesInRecyclerView();
            }
        });
    }

    private void displayMoviesInRecyclerView() {
        recyclerView = binding.recyclerView;
        movieAdapter = new MovieAdapter(this, movies);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter.notifyDataSetChanged();
    }
}