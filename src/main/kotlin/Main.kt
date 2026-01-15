package org.example

fun main() {

    val service = AirportService()

    service.fetchAndProcessAirports("src/main/resources/airports.txt")
}