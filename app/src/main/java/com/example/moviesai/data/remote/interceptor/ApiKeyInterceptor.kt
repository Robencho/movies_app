package com.example.moviesai.data.remote.interceptor

import com.example.moviesai.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val urlWithApiKey = originalUrl.newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(urlWithApiKey)
            .build()

        return chain.proceed(newRequest)
    }
}
