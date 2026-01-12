package org.example

import jakarta.xml.bind.JAXBContext
import org.example.netex.Airport
import java.io.StringReader

class AvinorScheduleXmlHandler {

    fun unmarshall(xmlData: String): Airport {
        val context = JAXBContext.newInstance(Airport::class.java)
        val unmarshaller = context.createUnmarshaller()

        try {
            val airportData: Airport = unmarshaller.unmarshal(StringReader(xmlData)) as Airport

            return airportData
        } catch (e: Exception) {
            throw RuntimeException("Error parsing Airport", e)
        }
    }

}