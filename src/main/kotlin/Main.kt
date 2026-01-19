package org.example

import Endpoint
import handler.*
import java.time.Instant
import model.avinorApi.Airport
import org.entur.siri.validator.SiriValidator
import java.io.File
import siri.SiriETMapper
import siri.SiriETPublisher
import java.time.Clock
import siri.validator.XsdValidator
import routes.api.AvinorApiHandler

//Temporary function to test JAXB objects fetched and made from Avinor api data
fun parseAndPrintFlights(airportData: Airport) {

    try {
        println("Flyplass: ${airportData.name}")
        val avinorApiHandler = AvinorApiHandler()
        if (airportData.flightsContainer?.lastUpdate != null){
            val lastUpdate : String = airportData.flightsContainer?.lastUpdate !! //forces not null
            val userCorrectDate = avinorApiHandler.userCorrectDate(lastUpdate)
            println("Sist oppdatert: $userCorrectDate")
        }

        val cache = AirlineNameHandler()
        airportData.flightsContainer?.flight?.forEach { flight ->
            println("Fly: ${if(flight.airline != null){cache.getName(flight.airline !!)}else{}} with id; ${flight.flightId} to/from ${flight.airport} - Status: ${flight.status?.code ?: "N/A"}")
        }
    } catch (e: Exception) {
        println("Something went wrong while parsing: ${e.message}")
        e.printStackTrace()
    }
}

fun main() {
    val avinorApi = AvinorApiHandler()
    val avxh = AvinorScheduleXmlHandler()
    val endpoint = Endpoint()
    val cache = AirlineNameHandler()
    val siriMapper = SiriETMapper()
    val siriPublisher = SiriETPublisher()
    val clock = Clock.systemUTC()

    println("Please choose a airport")
    val chosenAirport = readln()

    val xmlData = avinorApi.avinorXmlFeedApiCall(chosenAirport, directionParam = "D", lastUpdateParam = Instant.now(clock), codeshareParam = true)
    if (xmlData != null && "Error" !in xmlData) {
        parseAndPrintFlights(avxh.unmarshallXmlToAirport(xmlData))
    } else {
        println("Failed to fetch XML data: ($xmlData)")
    }

    Thread.sleep(3000) // Wait for async response */

    val airportCode = chosenAirport
    val airport = avxh.unmarshallXmlToAirport(xmlData ?: "")

    // Convert to SIRI-ET format
    println("Converting to SIRI-ET format...")
    val siri = siriMapper.mapToSiri(airport, airportCode)

    // Generate XML output
    val siriXml = siriPublisher.toXml(siri)

    // SendsXml to endpoint(localhost 8080)
    endpoint.siriEtEndpoint(siriXml)

    // Save to file
    val outputFile = File("siri-et-output.xml")
    siriPublisher.toFile(siri, outputFile, formatOutput = true)
    println("SIRI-ET XML saved to: ${outputFile.absolutePath}")
    println()

    // Validate XML against SIRI-ET XSD
    val result = XsdValidator().validateSirixml(siriXml, SiriValidator.Version.VERSION_2_1)
    println(result.message)

    // Print sample of output
    println("=== SIRI-ET XML Output (first 2000 chars) ===")
    println(siriXml.take(2000))
    if (siriXml.length > 2000) {
        println("... (truncated, see ${outputFile.name} for full output)")
    }
}
