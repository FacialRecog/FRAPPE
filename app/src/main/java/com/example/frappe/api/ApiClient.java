package com.example.frappe.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.frappe.Constants.BASE_URL_API;


public class ApiClient {

    //private static final String BASE_URL = "http://ec2-18-222-248-60.us-east-2.compute.amazonaws.com/api/";
//    private static final String BASE_URL = "http://18.222.248.60/api/";   //local base url
//    private static final String BASE_URL = "https://extranet.stayhopper.com/api/";   //live base url
    private static final String BASE_URL_WEATHER = "http://api.openweathermap.org/data/2.5/";
    private static final String BASE_URL_COUNTRY = "https://restcountries.eu/rest/v2/";
    private static Retrofit retrofitNew = null;
    private static Retrofit retrofit = null;
    private static Retrofit retrofitWeather = null;
    private static Retrofit retrofitCountry = null;
    private static OkHttpClient.Builder httpClient = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient().build())
                    .build();
        }
        return retrofit;
    }
    public static Retrofit getClientNew() {
        if (retrofitNew == null) {
            retrofitNew = new Retrofit.Builder()
                    .baseUrl(BASE_URL_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient().build())
                    .build();
        }
        return retrofitNew;
    }

    public static Retrofit getClientWeather() {
        if (retrofitWeather == null) {
            retrofitWeather = new Retrofit.Builder()
                    .baseUrl(BASE_URL_WEATHER)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient().build())
                    .build();
        }
        return retrofitWeather;
    }

    public static Retrofit getCountries() {
        if (retrofitCountry == null) {
            retrofitCountry = new Retrofit.Builder()
                    .baseUrl(BASE_URL_COUNTRY)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient().build())
                    .build();
        }
        return retrofitCountry;
    }



    public static OkHttpClient.Builder getHttpClient() {
        if (httpClient == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(30000, TimeUnit.SECONDS);
            httpClient.readTimeout(30000, TimeUnit.SECONDS).build();
            httpClient.addInterceptor(logging);
        }
        return httpClient;
    }
}
