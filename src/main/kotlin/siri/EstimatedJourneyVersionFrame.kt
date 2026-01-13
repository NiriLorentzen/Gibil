package org.example.siri

import jakarta.xml.bind.annotation.XmlElement

class EstimatedJourneyVersionFrame {

    @XmlElement(name = "RecordedAtTime")
    var recordedAtTime: String? = null

    @XmlElement(name = "EstimatedVehicleJourney")
    var estimatedVehicleJourney: EstimatedVehicleJourney? = null //PROBS SHOULD BE LIST

    constructor()
}