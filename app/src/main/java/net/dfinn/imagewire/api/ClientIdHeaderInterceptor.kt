package net.dfinn.imagewire.api

import okhttp3.Interceptor
import okhttp3.Response

class ClientIdHeaderInterceptor(private val clientId: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader(AUTHORIZATION_HEADER, "$CLIENT_ID_PREFIX $clientId")
            .build()
        return chain.proceed(newRequest)
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val CLIENT_ID_PREFIX = "Client-ID"
    }
}