package org.example.siri

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.FIELD)
class FramedVehicleJourneyRef {

    @XmlElement(name = "DataFrameRef")
    var dataFrameRef: String? = null

    @XmlElement(name = "DatedVehicleJourneyRef")
    var datedVehicleJourneyRef: String? = null

    constructor()
}