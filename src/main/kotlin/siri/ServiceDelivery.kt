package org.example.siri

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement


@XmlAccessorType(XmlAccessType.FIELD)
class ServiceDelivery {

    @XmlElement(name = "ResponseTimestamp", namespace = "http://www.siri.org.uk/siri")
    var responseTimestamp: String? = null

    @XmlElement(name = "ProducerRef", namespace = "http://www.siri.org.uk/siri")
    var producerRef: String? = null

    @XmlElement(name = "EstimatedTimetableDelivery", namespace = "http://www.siri.org.uk/siri")
    var estimatedTimetableDelivery: EstimatedTimetableDelivery? = null
    constructor()
}