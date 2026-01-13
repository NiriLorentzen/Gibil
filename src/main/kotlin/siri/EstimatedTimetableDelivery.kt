package org.example.siri

import jakarta.xml.bind.annotation.XmlAttribute
import jakarta.xml.bind.annotation.XmlElement

class EstimatedTimetableDelivery {

    @XmlAttribute(name = "version")
    var version: String? = null

    @XmlElement(name = "ResponseTimestamp")
    var responseTimestap: String? = null

    @XmlElement(name = "EstimatedJourneyVersionFrame")
    var estimatedJourneyVersionFrame: EstimatedJourneyVersionFrame? = null // SHOULD BE LIST PROBS

    constructor()
}