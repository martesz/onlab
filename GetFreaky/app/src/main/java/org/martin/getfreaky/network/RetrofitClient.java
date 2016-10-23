package org.martin.getfreaky.network;


import org.martin.getfreaky.GlobalVariables;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by martin on 2016. 05. 15..
 */
public class RetrofitClient {

    public static String BASE_URL = "http://192.168.1.2:20285/getFreakyService/getFreakyService/";

    private Retrofit retrofit;

    public RetrofitClient(final GlobalVariables application) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String token = application.getCurrentToken();
                if(token == null) {
                    token = "not yet obtained";
                }

                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", token)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });
        OkHttpClient client = clientBuilder.build();

        retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public GetFreakyService createService() {
        return retrofit.create(GetFreakyService.class);
    }
}
