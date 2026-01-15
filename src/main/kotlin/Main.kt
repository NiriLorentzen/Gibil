package org.example

import org.example.netex.Airport
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


fun parseAndPrintFlights(airportData: Airport) {
    try {
        println("\n==================================================")
        println("AIRPORT: ${airportData.name} (Last updated: ${airportData.flightsContainer?.lastUpdate})")
        println("==================================================")
        // Header for columns
        println("%-10s %-5s %-15s %-10s %-20s".format("FLIGHT", "DIR", "DESTINATION", "TIME", "STATUS"))
        println("--------------------------------------------------")

        airportData.flightsContainer?.flight?.forEach { flight ->

            val direction = if (flight.arrDep == "A") "From(A)" else "To(D)"


            val scheduledTime = flight.scheduleTime?.substring(11, 16) ?: "??"
            val newTime = flight.status?.time?.substring(11, 16)

            val statusText = when(flight.status?.code) {
                "A" -> "Arrival ($newTime)"
                "D" -> "Departure ($newTime)"
                "E" -> "New Time: $newTime"
                "C" -> "Cancelled"
                else -> ""
            }

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