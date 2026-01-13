package org.example.siri

import jakarta.xml.bind.annotation.XmlElement

class FramedVehicleJourneyRef {

    @XmlElement(name = "DataFrameRef")
    var dataFrameRef: String? = null

    @XmlElement(name = "DatedVehicleJourneyRef")
    var datedVehicleJourneyRef: String? = null

    constructor()
}