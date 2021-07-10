package com.codepath.instagram.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.instagram.R;
import com.codepath.instagram.models.Post;
import com.parse.GetDataCallback;
import com.parse.ParseException;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ViewHolder> {

    Context context;
    List<Post> posts;

    public ProfilePostAdapter(Context context, List<Post> posts) {
        super();
        this.context = context;
        this.posts = posts;
    }

    @Override
    public ProfilePostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile_post, parent, false);
        return new ProfilePostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePostAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfilePost;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProfilePost = itemView.findViewById(R.id.ivProfilePost);
        }

        public void bind(final Post post) {
            if (post.getImage() != null) {
                post.getImage().getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                            Glide.with(context).load(bmp).into(ivProfilePost);
                            ivProfilePost.setVisibility(VISIBLE);
                        } else {
                            Log.e("PostAdapter", "Image not loading", e);
                        }
                    }
                });
            } else {
                ivProfilePost.setVisibility(GONE);
            }
        }

    }
}
