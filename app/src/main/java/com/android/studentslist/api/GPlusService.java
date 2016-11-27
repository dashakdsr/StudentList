package com.android.studentslist.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GPlusService {
    public final GPlusApi api;

    public GPlusService() {
        final String BASE_URL = "https://www.googleapis.com";
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GPlusApi.class);
    }
}
