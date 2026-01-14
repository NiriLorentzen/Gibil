package org.example.siri

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.FIELD)
class FramedVehicleJourneyRef {

    @XmlElement(name = "DataFrameRef", namespace = "http://www.siri.org.uk/siri")
    var dataFrameRef: String? = null

    @XmlElement(name = "DatedVehicleJourneyRef", namespace = "http://www.siri.org.uk/siri")
    var datedVehicleJourneyRef: String? = null

    constructor()
}