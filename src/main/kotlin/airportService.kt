package org.example

import org.example.netex.Airport //model.avinorApi.flight
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis
import java.io.File

class AirportService(
    private val api: AvinorApiHandling = AvinorApiHandling(),
    private val xmlHandler: AvinorScheduleXmlHandler = AvinorScheduleXmlHandler()
) {


    fun fetchAndProcessAirports(filePath: String) = runBlocking {

        val file = File(filePath)
        if (!file.exists()) {
            println("Error: Could not find airport list at $filePath")
            return@runBlocking
        }

        val airportCodes = file.readLines().filter { it.isNotBlank() }
        println("Starting data fetch for ${airportCodes.size} airports...")

        val timeUsed = measureTimeMillis {
            airportCodes.chunked(5).forEach { batch ->
                processBatch(batch)
            }
        }

        println("\n==================================================")
        println("Total operation time: $timeUsed ms")
    }


    private suspend fun processBatch(batch: List<String>) = coroutineScope {
        val deferredResults = batch.map { code ->
            async(Dispatchers.IO) {
                delay(50)
                println("Sending request for $code")
                code to api.avinorXmlFeedApiCall(
                    airportCodeParam = code,
                    timeFromParam = 2,
                    timeToParam = 7,
                    directionParam = null,
                    lastUpdateParam = null
                )
            }
        }

        val results = deferredResults.awaitAll()

        results.forEach { (code, xmlData) ->
            if (xmlData != null && "Error" !in xmlData) {
                try {
                    val airportObject = xmlHandler.unmarshallAirportToXml(xmlData)
                    printFlightDetails(airportObject)
                } catch (e: Exception) {
                    println("Could not parse data for $code: ${e.message}")
                }
            } else {
                println("Failed to fetch data for $code")
            }
        }
    }


    private fun printFlightDetails(airportData: Airport) {
        try {
            println("\n--------------------------------------------------")
            println("AIRPORT: ${airportData.name}")
            println("%-10s %-10s %-15s %-10s %-20s".format("FLIGHT", "DIR", "DEST", "TIME", "STATUS"))
            println("\n--------------------------------------------------")

            airportData.flightsContainer?.flight?.forEach { flight ->
                val direction = if (flight.arrDep == "A") "From(A)" else "To  (D)"
                val scheduledTime = flight.scheduleTime?.substring(11, 16) ?: "??"
                val newTime = flight.status?.time?.substring(11, 16)

                val statusText = when(flight.status?.code) {
                    "A" -> "Arrived ($newTime)"
                    "D" -> "Departed ($newTime)"
                    "E" -> "New Time: $newTime"
                    "C" -> "Cancelled"
                    else -> ""
                }

                println("%-10s %-10s %-15s %-10s %-20s".format(
                    flight.flightId,
                    direction,
                    flight.airport,
                    scheduledTime,
                    statusText
                ))
            }
        } catch (e: Exception) {
            println("Error printing flight details: ${e.message}")
        }
    }
}