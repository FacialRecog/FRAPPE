package com.example.frappe.utils;

import androidx.annotation.NonNull;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Commons {
    public static RequestBody createRequestBody(@NonNull String s) {
        return RequestBody.create(
                MediaType.parse("*/*"), s);
    }
    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }
}
