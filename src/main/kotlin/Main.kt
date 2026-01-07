package org.example


import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import okhttp3.Call
import okhttp3.Response

fun urlBuilder(airportCode: String): String {
    val baseurl = "https://asrv.avinor.no/XmlFeed/v1.0"
    val airport = "?airport=" + airportCode

    val url = baseurl + airport
    return url;
}


fun main() {
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(urlBuilder("OSL"))
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