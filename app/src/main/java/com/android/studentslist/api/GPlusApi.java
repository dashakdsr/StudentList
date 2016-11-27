package com.android.studentslist.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

@SuppressWarnings("WeakerAccess")
public interface GPlusApi {
    @GET("/plus/v1/people/{userId}")
    Call<GPlusUser> getUserInfo(
            @Path("userId") String userId,
            @Query("key") String apiKey
    );
}
