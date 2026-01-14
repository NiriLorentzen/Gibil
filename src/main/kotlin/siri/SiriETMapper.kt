package org.example.siri

import avinor.model.Airport
import avinor.model.Flight
import org.entur.siri.adapter.ZonedDateTimeAdapter
import uk.org.siri.siri21.*
import java.time.ZonedDateTime



class SiriETMapper {
    companion object {
        // Constants for SIRI mapping
        private const val PRODUCER_REF = "AVINOR"
        private const val DATA_SOURCE = "AVINOR"
        // SIRI reference prefixes
        private const val OPERATOR_PREFIX = "AVINOR:Operator:"
        private const val LINE_PREFIX = "AVINOR:Line:"
        private const val VEHICLE_JOURNEY_PREFIX = "AVINOR:ServiceJourney:"
        private const val STOP_PREFIX = "AVINOR:StopPlace:"
    }

    //Create SIRI element, populate header and add EstimatedTimetableDelivery to SIRI response
    fun mapToSiri(airport: Airport, requestingAirportCode: String): Siri {
        val siri = Siri()

        val serviceDelivery = ServiceDelivery()
        serviceDelivery.responseTimestamp = java.time.ZonedDateTime.now()

        val producerRef = RequestorRef()
        producerRef.value = PRODUCER_REF
        serviceDelivery.producerRef = producerRef

        val etDelivery = createEstimatedTimetableDelivery(airport, requestingAirportCode)
        serviceDelivery.estimatedTimetableDeliveries.add(etDelivery)

        siri.serviceDelivery = serviceDelivery
        return siri
    }
    private fun createEstimatedTimetableDelivery(
        airport: Airport, requestingAirportCode: String): EstimatedTimetableDeliveryStructure {
        val delivery = EstimatedTimetableDeliveryStructure()
        delivery.version = "2.1"
        delivery.responseTimestamp = java.time.ZonedDateTime.now()

        // create EstimatedJourneyVersionFrame element
        val estimatedVersionFrame = EstimatedVersionFrameStructure()
        estimatedVersionFrame.recordedAtTime = ZonedDateTimeAdapter.parse(airport.flightsContainer?.lastUpdate) ?: ZonedDateTime.now()

        // Map each flight to EstimatedVehicleJourney
        airport.flightsContainer?.flight?.forEach { flight ->
            val estimatedVehicleJourney = mapFlightToEstimatedVehicleJourney(flight, requestingAirportCode)
            if (estimatedVehicleJourney != null) {
                estimatedVersionFrame.estimatedVehicleJourneies.add(estimatedVehicleJourney)
            }
        }
        delivery.estimatedJourneyVersionFrames.add(estimatedVersionFrame)
        return delivery
    }
    private fun mapFlightToEstimatedVehicleJourney(
        flight: Flight, requestingAirportCode: String): EstimatedVehicleJourney? {

        // Skip flights without a flightId
        if (flight.flightId == null) return null
        val airline = flight.airline ?: return null
        val scheduleTime = ZonedDateTimeAdapter.parse(flight.scheduleTime) ?: return null

        val estimatedVehicleJourney = EstimatedVehicleJourney()

        //Set lineRef
        val lineRef = LineRef()
        //TODO! find out proper lineRef content (for now just use prefix + airline code)
        lineRef.value = "$LINE_PREFIX$airline"
        estimatedVehicleJourney.lineRef = lineRef

        //Set directionRef
        val directionRef = DirectionRefStructure()
        directionRef.value = if (flight.isDeparture()) "outbound" else "inbound"
        estimatedVehicleJourney.directionRef = directionRef

        //Set FramedVehicleJourneyRef
        val framedVehicleJourneyRef = FramedVehicleJourneyRefStructure()
        val dataFrameRef = DataFrameRefStructure()
        dataFrameRef.value = scheduleTime.toLocalDate().toString()
        framedVehicleJourneyRef.dataFrameRef
        //TODO! find out proper vehicleJourneyRef content (for now just use prefix + uniqueID)
        framedVehicleJourneyRef.datedVehicleJourneyRef = VEHICLE_JOURNEY_PREFIX + flight.uniqueID
        estimatedVehicleJourney.framedVehicleJourneyRef = framedVehicleJourneyRef

        estimatedVehicleJourney.dataSource = DATA_SOURCE
        //TODO! Find out what to do with ExtraJourney
        //estimatedVehicleJourney.extraJourney(false)
        estimatedVehicleJourney.isCancellation

        val operatorRef = OperatorRefStructure()
        operatorRef.value = "$OPERATOR_PREFIX$airline"
        estimatedVehicleJourney.operatorRef = operatorRef

        return estimatedVehicleJourney
    }

}