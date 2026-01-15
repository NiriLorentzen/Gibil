package org.example

import org.example.netex.Airport
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/*
//Temporary function to test JAXB objects fetched and made from Avinor api data
fun parseAndPrintFlights(airportData: Airport) {

    try {
        println("Airport: ${airportData.name}")
        println("Last updated: ${airportData.flightsContainer?.lastUpdate ?: "N/A"}")

        airportData.flightsContainer?.flight?.forEach { flight ->
            println("Flight: ${flight.flightId} to/from ${flight.airport} - Status: ${flight.status?.code ?: "N/A"}")
        }
    } catch (e: Exception) {
        println("Something went wrong while parsing: ${e.message}")
        e.printStackTrace()
    }
}
*/

fun parseAndPrintFlights(airportData: Airport) {
    try {
        println("\n==================================================")
        println("AIRPORT: ${airportData.name} (Last updated: ${airportData.flightsContainer?.lastUpdate})")
        println("==================================================")
        // Header for columns
        println("%-10s %-5s %-15s %-10s %-20s".format("FLIGHT", "DIR", "DESTINATION", "TIME", "STATUS"))
        println("--------------------------------------------------")

        airportData.flightsContainer?.flight?.forEach { flight ->

            // 1. Determine if it is Departure (To) or Arrival (From)
            val direction = if (flight.arrDep == "A") "From(A)" else "To(D)"

            // 2. Make timestamps readable (we clip out the time HH:mm from the date string)
            // Example string: "2026-01-15T08:48:50..." -> We take characters 11 to 16 to get "08:48"
            val scheduledTime = flight.scheduleTime?.substring(11, 16) ?: "??"
            val newTime = flight.status?.time?.substring(11, 16)

            // 3. Translate status code to understandable English
            val statusText = when(flight.status?.code) {
                "A" -> "Arrival ($newTime)"
                "D" -> "Departure ($newTime)"
                "E" -> "New Time: $newTime"  // The time appears here!
                "C" -> "Cancelled"
                else -> "" // No status often means it is on schedule
            }

            // 4. Print it out in nice columns
            println("%-10s %-5s %-15s %-10s %-20s".format(
                flight.flightId,
                direction,
                flight.airport,
                scheduledTime,
                statusText
            ))
        }
    } catch (e: Exception) {
        println("Something went wrong during printing: ${e.message}")
        e.printStackTrace()
    }
}

fun main() = runBlocking {

    val airportCodes = listOf("OSL", "BGO")
    val AVXH = AvinorScheduleXmlHandler()

    val api = AvinorApiHandling()

    println("Starting data fetch for ${airportCodes.size} airports...")

    val timeUsed = measureTimeMillis {
        val deferredResults: List<Deferred<String?>> = airportCodes.map { code ->
            async(Dispatchers.IO) {
                println("Sending request for $code")
                api.avinorXmlFeedApiCall(
                    airportCodeParam = code,
                    timeFromParam = 1,
                    timeToParam = 8,
                    directionParam = null,
                    lastUpdateParam = null

                )
            }
        }

        val results: List<String?> = deferredResults.awaitAll()

        results.forEachIndexed { index, xmlData ->
            if (xmlData != null && "Error" !in xmlData) {
                println("Got data for ${airportCodes[index]}: ${xmlData.take(60)}...")

                try {
                    val airportObject = AVXH.unmarshall(xmlData)
                    parseAndPrintFlights(airportObject)
                } catch (e: Exception) {
                    println("Could not read data for ${airportCodes[index]}")
                }
            }
            else {
                println("Failed for ${airportCodes[index]}")
            }
        }
    }

        println("Total time: $timeUsed ms")
}


/*
    var AVXH = AvinorScheduleXmlHandler()

    println("Please choose a airport")
    val chosenAirport = readln()
    val avinorApi = AvinorApiHandling()
    val specificTime = Instant.parse("2024-08-08T09:30:00Z")

    val exampleQueryAPI = avinorApi.avinorXmlFeedApiCall(
        airportCodeParam = chosenAirport,
        directionParam = "A",
        lastUpdateParam = specificTime,
        serviceTypeParam = "E",
        timeToParam = 336,
        timeFromParam = 24,
    )

    val xmlData = avinorApi.avinorXmlFeedApiCall(chosenAirport, directionParam = "D", lastUpdateParam = Instant.now())
    if (xmlData != null && "Error" !in xmlData) {
        parseAndPrintFlights(AVXH.unmarshall(xmlData))
    } else {
        println("Failed to fetch XML data: ($xmlData)")
    }

    Thread.sleep(3000) // Wait for async response
*/