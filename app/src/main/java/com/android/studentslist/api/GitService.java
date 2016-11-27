package com.android.studentslist.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitService {
    public final GitApi api;

    public GitService() {
        final String BASE_URL = "https://api.github.com";
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GitApi.class);
    }
}