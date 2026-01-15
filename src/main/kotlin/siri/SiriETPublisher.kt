package siri

import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.Marshaller
import uk.org.siri.siri21.Siri
import java.io.StringWriter
import java.io.File

class SiriETPublisher {
    private val jaxbContext = JAXBContext.newInstance(Siri::class.java)

    fun toXml(siri: Siri, formatOutput: Boolean = true): String {
        val marshaller = jaxbContext.createMarshaller()

        if (formatOutput) {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        }

        val writer = StringWriter()
        marshaller.marshal(siri, writer)
        return writer.toString()
    }

    fun toFile(siri: Siri, file: File, formatOutput: Boolean) {
        val marshaller = jaxbContext.createMarshaller()

        if (formatOutput) {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        }

        marshaller.marshal(siri, file)
    }
}