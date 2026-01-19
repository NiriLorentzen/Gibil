import config.App

import java.time.Instant
import org.entur.siri.validator.SiriValidator
import siri.validator.ValidationResult

class SiriEtService(private val components: App) {

    private val DEPATURE_CODE = "D" //Should be const and moved

    // Should be switched with iterating through all aiports
    fun fetchAndConvert(airportCode: String): String {
        val xmlData = components.avinorApi.avinorXmlFeedApiCall(
            airportCode,
            directionParam = DEPATURE_CODE,
            lastUpdateParam = Instant.now(components.clock),
            codeshareParam = true
        )

        val airport = components.avxh.unmarshallXmlToAirport(xmlData ?: "")
        val siri = components.siriMapper.mapToSiri(airport, airportCode)

        return components.siriPublisher.toXml(siri)
    }

    fun validateXmlXsd(siriXml: String): ValidationResult {
        return components.xsdValidator.validateSirixml(siriXml, SiriValidator.Version.VERSION_2_1)
    }


}