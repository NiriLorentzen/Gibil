package org.example

import config.App
import routes.api.Endpoint
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
    val components = App()

    println("Please choose a airport")
    val chosenAirport = readln()
    val xmlData = avinorApi.avinorXmlFeedApiCall(chosenAirport, directionParam = "D", lastUpdateParam = Instant.now(clock), codeshareParam = true)


    val airportCode = chosenAirport
    val airport = avxh.unmarshallXmlToAirport(xmlData ?: "")

    val siri = siriMapper.mapToSiri(airport, airportCode)

    // Generate XML output
    val siriXml = siriPublisher.toXml(siri)

    // SendsXml to endpoint(localhost 8080)
    endpoint.siriEtEndpoint(siriXml)

    // Save to file
    val outputFile = File("siri-et-output.xml")
    siriPublisher.toFile(siri, outputFile, formatOutput = true)

    // Validate XML against SIRI-ET XSD
    val result = XsdValidator().validateSirixml(siriXml, SiriValidator.Version.VERSION_2_1)
    println(result.message)
}
