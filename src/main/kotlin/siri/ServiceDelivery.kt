package org.example.siri

import jakarta.xml.bind.annotation.XmlElement

class ServiceDelivery {

    @XmlElement(name = "ResponseTimestamp")
    var responseTimestamp: String? = null

    @XmlElement(name = "ProducerRef")
    var producerRef: String? = null

    @XmlElement(name = "EstimatedTimetableDelivery")
    var estimatedTimetableDelivery: EstimatedTimetableDelivery? = null//SHOULD PROBABLY BE LIST

    constructor()
}