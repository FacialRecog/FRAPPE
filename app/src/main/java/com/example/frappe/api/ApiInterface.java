package com.example.frappe.api;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("users/checklogin")
    Call<JsonObject> login(@Field("username") String name, @Field("password") String password);



    @Multipart
    @POST("upload/")
    Call<JsonObject> register( @PartMap Map<String, RequestBody> params,
                                   @Part MultipartBody.Part image);

    @Multipart
    @POST("Attendance/")
    Call<JsonObject> scan( @Part MultipartBody.Part image);
}