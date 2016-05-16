package org.martin.getfreaky.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by martin on 2016. 05. 15..
 */
public class RetrofitClient {

    public static String BASE_URL = "http://192.168.1.2:8080/getFreakyService/getFreakyService/";

    private Retrofit retrofit;

    public RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }

    public GetFreakyService createService(){
        return retrofit.create(GetFreakyService.class);
    }
}
