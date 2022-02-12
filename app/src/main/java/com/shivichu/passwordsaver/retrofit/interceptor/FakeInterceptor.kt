package com.shivichu.passwordsaver.retrofit.interceptor

import com.shivichu.passwordsaver.BuildConfig
import okhttp3.*
import java.io.IOException
import kotlin.jvm.Throws

class FakeInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response? {
                val response: Response?
                if (BuildConfig.DEBUG) {
                        //val uri = chain.request().url().uri().toString()
                        val responseString = when {
                                //uri.endsWith("tourists") -> touristsJson
                                //uri.endsWith("countries") -> countriesJson
                                else -> ""
                        }

                        response = Response.Builder()
                                .code(200)
                                .message(responseString)
                                .request(chain.request())
                                .protocol(Protocol.HTTP_1_0)
                                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.toByteArray()))
                                .addHeader("content-type", "application/json")
                                .build()
                } else {
                        response = chain.proceed(chain.request())
                }

                return response
        }
}
