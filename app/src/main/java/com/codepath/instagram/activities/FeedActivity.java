package com.codepath.instagram.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.PostAdapter;
import com.codepath.instagram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.reverse;

public class FeedActivity extends AppCompatActivity {

    RecyclerView rvFeed;
    List<Post> posts;
    PostAdapter adapter;

    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.optPost:
                        Log.d("onNavigationItemSelected", "optPost");
                        Intent i = new Intent(FeedActivity.this, CreatePostActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.optProfile:
                        Intent j = new Intent(FeedActivity.this, ProfileActivity.class);
                        startActivity(j);
                        return true;
                    default: return true;
                }
            }
        });

        posts = new ArrayList<>();
        adapter = new PostAdapter(this, posts);

        rvFeed = findViewById(R.id.rvFeed);
        rvFeed.setLayoutManager(new LinearLayoutManager(this));
        rvFeed.setAdapter(adapter);
        queryPosts();

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logOut) {
            ParseUser.logOut();
            Intent i  = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
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

    public void fetchTimelineAsync(int page) {
        // Remember to CLEAR OUT old items before appending in the new ones
        adapter.clear();
        // ...the data has come back, add new items to your adapter...
        queryPosts();
        swipeContainer.setRefreshing(false);
    }

}