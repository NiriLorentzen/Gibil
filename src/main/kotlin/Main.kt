package org.example

import com.sun.net.httpserver.Request
import okhttp3.*
import java.io.IOException



fun main() {
    val client = OkHttpClient()

    val request = Request.Builder()
        .url("https://jsonplaceholder.typicode.com/posts/1")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            println("Request failed: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (response.isSuccessful) {
                    println("Response: ${response.body?.string()}")
                } else {
                    println("Error: ${response.code}")
                }
            }
        }
    })

    Thread.sleep(3000) // Wait for async response
}