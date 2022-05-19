package com.teckzy.msrsilverhouse.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class APIClient {
    private static Retrofit retrofit;
    public static final String BASE_URL = "http://msr.teckzy.co.in/";

    public static Retrofit getAPIClient()
    {
        if (retrofit == null)
        {
            retrofit = new retrofit2.Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}