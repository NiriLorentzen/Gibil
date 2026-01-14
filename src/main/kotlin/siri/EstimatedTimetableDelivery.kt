package org.example.siri

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlAttribute
import jakarta.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.FIELD)
class EstimatedTimetableDelivery {

    @XmlAttribute(name = "version")
    var version: String? = null

    @XmlElement(name = "ResponseTimestamp", namespace = "http://www.siri.org.uk/siri")
    var responseTimestap: String? = null

    @XmlElement(name = "EstimatedJourneyVersionFrame", namespace = "http://www.siri.org.uk/siri")
    var estimatedJourneyVersionFrame: List<EstimatedJourneyVersionFrame> = mutableListOf()


    constructor()
}