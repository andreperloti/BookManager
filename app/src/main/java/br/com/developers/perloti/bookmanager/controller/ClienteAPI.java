package br.com.developers.perloti.bookmanager.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by perloti on 17/06/18.
 */

public interface ClienteAPI {

    @GET("volumes?maxResults=40")
    Call<LinkedTreeMap> getBook(@Query("q") String q);

    @GET("volumes/{id}")
    Call<LinkedTreeMap> getBookById(@Path("id") String id);

    class MyRetrofit {
        private static String BASE_URL = "https://www.googleapis.com/books/v1/";

        static ClienteAPI getInstance() {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();

            return retrofit.create(ClienteAPI.class);
        }
    }

}
