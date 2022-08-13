package com.example.modernizedshapp.doctor.HealthNews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.modernizedshapp.R;
import com.example.modernizedshapp.User.AllCategories;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HealthMainActivity extends AppCompatActivity implements CategoryRvAdapter.CategoryClickInterface {
    //4106b4135fa942cabd50160b0887817f

    private RecyclerView newsRv, categoryRv;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRvModel> categoryRvModelsArrayList;
    private CategoryRvAdapter categoryRvAdapter;
    private NewsRvAdapter newsRvAdapter;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_main);
        //Hooks
        backBtn = findViewById(R.id.back_pressed);
        backBtn.setOnClickListener(v -> HealthMainActivity.super.onBackPressed());
        newsRv = findViewById(R.id.RvNews);
        categoryRv = findViewById(R.id.RvNcategories);
        loadingPB = findViewById(R.id.Rvprogressbar);
        articlesArrayList = new ArrayList<>();
        categoryRvModelsArrayList = new ArrayList<>();
        newsRvAdapter = new NewsRvAdapter(articlesArrayList, this);
        categoryRvAdapter = new CategoryRvAdapter(categoryRvModelsArrayList, this, this::onCategoryClick);
        newsRv.setLayoutManager(new LinearLayoutManager(this));
        newsRv.setAdapter(newsRvAdapter);
        categoryRv.setAdapter(categoryRvAdapter);
        getCategories();
        getNews("All");
        newsRvAdapter.notifyDataSetChanged();

    }

    private void getCategories() {
        categoryRvModelsArrayList.add(new CategoryRvModel("All", "https://images.unsplash.com/photo-1540575467063-178a50c2df87?ixid=MnwxMjA3fDB8MHxzZWFyY2h8N3x8bmV3c3xlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
        categoryRvModelsArrayList.add(new CategoryRvModel("Health", "https://images.unsplash.com/photo-1445527815219-ecbfec67492e?ixid=MnwxMjA3fDB8MHxzZWFyY2h8NjJ8fGhlYWx0aGNhcmV8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
        categoryRvModelsArrayList.add(new CategoryRvModel("Science", "https://images.unsplash.com/photo-1567427018141-0584cfcbf1b8?ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8c2NpZW5jZXxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
        categoryRvAdapter.notifyDataSetChanged();
    }

    private void getNews(String category) {
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "http://newsapi.org/v2/top-headlines?country=us&category=" + category + "&apiKey=4106b4135fa942cabd50160b0887817f";
        String url = "https://newsapi.org/v2/everything?q=Apple&from=2021-08-11&sortBy=popularity&apiKey=4106b4135fa942cabd50160b0887817f";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModel> call;
        if (category.equals("All")) {
            call = retrofitAPI.getAllNews(url);
        } else {
            call = retrofitAPI.getNewsByCategory(categoryURL);
        }
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModel.getArticles();
                for (int i = 0; i < articles.size(); i++) {
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(), articles.get(i).getDescription(), articles.get(i).getUrlToImage(),
                            articles.get(i).getUrl(), articles.get(i).getContent()));
                }
                newsRvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable throwable) {
                Toast.makeText(HealthMainActivity.this, "Fail to load requested news", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRvModelsArrayList.get(position).getCategory();
        getNews(category);

    }
}