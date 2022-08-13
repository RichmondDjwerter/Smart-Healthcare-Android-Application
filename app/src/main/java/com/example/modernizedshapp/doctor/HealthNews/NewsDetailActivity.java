package com.example.modernizedshapp.doctor.HealthNews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.modernizedshapp.R;
import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

    String title, description, content, imageURL, url;
    private TextView titleTv, subdescTv, contentTv;
    private Button readnewsbtn;
    private ImageView newsTv, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        backBtn = findViewById(R.id.back_pressed);
        backBtn.setOnClickListener(v -> NewsDetailActivity.super.onBackPressed());
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        description = getIntent().getStringExtra("description");
        imageURL = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");
        titleTv = findViewById(R.id.TVTitle);
        subdescTv = findViewById(R.id.TVsubDescription);
        contentTv = findViewById(R.id.TVContent);
        readnewsbtn = findViewById(R.id.BtnReadNews);
        newsTv = findViewById(R.id.idTVNews);
        titleTv.setText(title);
        subdescTv.setText(description);
        contentTv.setText(content);
        Picasso.get().load(imageURL).into(newsTv);
        readnewsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}