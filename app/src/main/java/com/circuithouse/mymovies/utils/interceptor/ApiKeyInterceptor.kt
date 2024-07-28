package com.circuithouse.mymovies.utils.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

// Define the TMDB API key as a constant
private const val API_KEY = "a97d22384f177efa384911228021b649"

// Custom interceptor to add the API key to every request
class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Get the original request
        val request = chain.request()

        // Add the API key as a query parameter to the original URL
        val url = request.url.newBuilder().addQueryParameter(TMDB_API_KEY, API_KEY).build()

        // Create a new request with the updated URL
        val newRequest = request.newBuilder().url(url).build()

        // Proceed with the new request
        return chain.proceed(newRequest)
    }

    companion object {
        // Constant for the query parameter key
        private const val TMDB_API_KEY = "api_key"
    }
}
