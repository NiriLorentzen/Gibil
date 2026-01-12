package org.example

import java.time.Instant

fun main() {
    val flyplass = readln()

    val avinorApi = AvinorApiHandling()

    val specificTime = Instant.parse("2024-08-08T09:30:00Z")

    val exampleQueryAPI = avinorApi.apiCall(
        airportCodeParam = flyplass,
        directionParam = "A",
        lastUpdateParam = specificTime,
        serviceTypeParam = "E",
        timeToParam = 336,
        timeFromParam = 24,
    )

    val xmlData = avinorApi.apiCall("OSL", directionParam = "D", lastUpdateParam = Instant.now())
    if (xmlData != null) {
        // Send to XML handling function
    } else {
        println("Failed to fetch XML data")
    }

    println(exampleQueryAPI)
}