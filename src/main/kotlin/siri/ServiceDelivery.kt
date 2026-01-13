package org.example.siri

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.FIELD)
class ServiceDelivery {

    @XmlElement(name = "ResponseTimestamp")
    var responseTimestamp: String? = null

    @XmlElement(name = "ProducerRef")
    var producerRef: String? = null

    @XmlElement(name = "EstimatedTimetableDelivery")
    var estimatedTimetableDelivery: List<EstimatedTimetableDelivery> = emptyList()

    constructor()
}