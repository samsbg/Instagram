package com.codepath.instagram;

import android.app.Application;

import com.codepath.instagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Xqz9EqoBy1OQ2kOHXIRqHkeWlE47cy3lVqgTBp2H")
                .clientKey("6QOSavAU3qZ2oKU5lS7X5cn90bEpxzIFidTaXJxZ")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
