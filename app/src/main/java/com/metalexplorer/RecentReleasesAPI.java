package com.metalexplorer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecentReleasesAPI {
    @GET("/albums")
    Call<List<RecentReleasesData>> getAllData();
}
