package org.example.siri

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.FIELD)
class EstimatedVehicleJourney {

    @XmlElement(name = "RecordedAtTime")
    var recordedAtTime: String? = null

    @XmlElement(name = "LineRef")
    var lineRef: String? = null

    @XmlElement(name = "DirectionRef")
    var directionRef: String? = null //SHOUMD MBY BE INT ??

    @XmlElement(name = "FramedVehicleJourneyRef")
    var framedVehicleJourneyRef: List<FramedVehicleJourneyRef> = emptyList()

    constructor()
}