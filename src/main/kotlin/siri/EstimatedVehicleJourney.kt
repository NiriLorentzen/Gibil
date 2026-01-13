package org.example.siri

import jakarta.xml.bind.annotation.XmlElement

class EstimatedVehicleJourney {

    @XmlElement(name = "RecordedAtTime")
    var recordedAtTime: String? = null

    @XmlElement(name = "LineRef")
    var lineRef: String? = null

    @XmlElement(name = "DirectionRef")
    var directionRef: String? = null //SHOUMD MBY BE INT ??

    @XmlElement(name = "FramedVehicleJourneyRef")
    var framedVehicleJourneyRef: FramedVehicleJourneyRef? = null //PROBS SHOULD BE LIST

    constructor()
}