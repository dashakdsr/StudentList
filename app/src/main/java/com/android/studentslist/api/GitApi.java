package com.android.studentslist.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitApi {
    @GET("/users/{userId}")
    Call<GitUser> getUserInfo(
            @Path("userId") String userId);
}
