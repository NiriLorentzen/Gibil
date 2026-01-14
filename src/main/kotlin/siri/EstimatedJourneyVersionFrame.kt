package org.example.siri

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.FIELD)
class EstimatedJourneyVersionFrame {

    @XmlElement(name = "RecordedAtTime", namespace = "http://www.siri.org.uk/siri")
    var recordedAtTime: String? = null

    @XmlElement(name = "EstimatedVehicleJourney", namespace = "http://www.siri.org.uk/siri")
    var estimatedVehicleJourney: List<EstimatedVehicleJourney> = mutableListOf()

    constructor()
}