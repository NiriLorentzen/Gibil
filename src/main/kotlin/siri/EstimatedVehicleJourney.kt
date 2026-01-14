package org.example.siri

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.FIELD)
class EstimatedVehicleJourney {

    @XmlElement(name = "RecordedAtTime", namespace = "http://www.siri.org.uk/siri")
    var recordedAtTime: String? = null

    @XmlElement(name = "LineRef", namespace = "http://www.siri.org.uk/siri")
    var lineRef: String? = null

    @XmlElement(name = "DirectionRef", namespace = "http://www.siri.org.uk/siri")
    var directionRef: String? = null //SHOUMD MBY BE INT ??

    @XmlElement(name = "FramedVehicleJourneyRef", namespace = "http://www.siri.org.uk/siri")
    var framedVehicleJourneyRef: List<FramedVehicleJourneyRef> = mutableListOf()


    constructor()
}