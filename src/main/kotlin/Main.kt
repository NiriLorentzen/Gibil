package org.example

import SiriEtService
import config.App
import routes.api.Endpoint


fun main() {
    val components = App()
    val service = SiriEtService(components)

    //Should be removed when calling all airports.
    print("Please choose an airport: ")
    val airportCode = readln()

    //Should be changed into calling all airports
    val siriXml = service.fetchAndConvert(airportCode)

    val validationResult = service.validateXmlXsd(siriXml)
    println(validationResult.message)

    //Should maybe be put into App.kt
    Endpoint().siriEtEndpoint(siriXml)
}
