package com.codepath.instagram.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.ProfilePostAdapter;
import com.codepath.instagram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.reverse;

public class ProfileActivity extends AppCompatActivity {

    TextView tvUsername;
    RecyclerView rvUserFeed;
    Button btnProfilePic;

    List<Post> posts;
    ProfilePostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.optPost:
                        Intent i = new Intent(ProfileActivity.this, CreatePostActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.optFeedView:
                        Intent j = new Intent(ProfileActivity.this, FeedActivity.class);
                        startActivity(j);
                        return true;
                    default: return true;
                }
            }
        });

        posts = new ArrayList<>();
        adapter = new ProfilePostAdapter(this, posts);

        tvUsername = findViewById(R.id.tvUsername);
        rvUserFeed = findViewById(R.id.rvUserFeed);
        btnProfilePic = findViewById(R.id.btnProfilePic);

        tvUsername.setText(ParseUser.getCurrentUser().getUsername());

        rvUserFeed.setLayoutManager(new GridLayoutManager(this, 3));
        rvUserFeed.setAdapter(adapter);
        queryProfilePosts();

        btnProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void queryProfilePosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> postsList, com.parse.ParseException e) {
                if (e != null) {
                    Log.e("FeedActivity", "Issue with getting posts", e);
                    return;
                }
                reverse(postsList);
                posts.addAll(postsList);
                adapter.notifyDataSetChanged();
            }
        });
    }
}