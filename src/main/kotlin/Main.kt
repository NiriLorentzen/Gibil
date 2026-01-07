package org.example


import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import okhttp3.Call
import okhttp3.Response

fun urlBuilder(airportCode: String, timeFrom: String = "0", timeTo: String = "1", direction: String = ""): String {
    /*
     Makes a complete url for the api to use based on the avinor api.
     Obligatory parameters: airport code, example: OSL
     non-obligatory parameters:
        timeFrom, what flights you fetch backwards in time, counts in hours
        timeTo, what flights you fetch forwards in time, counts in hours
        direction, shows either arrival flights, departure flights, or both. "A" shows only arrivals, "D" shows only departures, nothing will show both.
        lastUpdate, shows flightdata only from after a set datetime
        codeshare, adds possible codeshare information to the flightdata, consists of: codeshareAirlineDesignators, codeshareAirlineNames, codeshareFlightNumbers and codeshareOperationalSuffixs.
        serviceType, option to differentiate based on flight type, such as helicopter(E), regular flights(J), charter flights(C)
    */
    val baseurl = "https://asrv.avinor.no/XmlFeed/v1.0"
    val airport = "?airport=" + airportCode
    val time = "&TimeFrom=" + timeFrom + "&TimeTo=" + timeTo

    //formats direction-information if a valid direction is specified, else sets it to be nothing
    val directionFinal = if (direction != "" && (direction == "D" || direction == "A")) {
        "&Direction=" + direction
    } else {
        ""
    }


    val url = baseurl + airport + time + directionFinal
    return url;
}


fun main() {
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(urlBuilder("OSL", direction="A"))
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