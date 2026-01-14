package org.example.siri

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlAttribute
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "Siri", namespace = "http://www.siri.org.uk/siri")
@XmlAccessorType(XmlAccessType.FIELD)
class Siri {

    @XmlAttribute(name = "version")
    lateinit var version: String

    @XmlElement(name = "ServiceDelivery", namespace = "http://www.siri.org.uk/siri")
    var serviceDelivery: ServiceDelivery? = null

    constructor()
}