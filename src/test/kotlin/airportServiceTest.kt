package org.example

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.File

/**
 * Test class for AirportService.
 */
class AirportServiceTest {

    /**
     * A fake implementation of AvinorApiHandling for testing purposes.
     */
    class FakeAvinorApi : AvinorApiHandler() {

        val requestedAirports: MutableList<String> =
            java.util.Collections.synchronizedList(mutableListOf<String>())

        override fun avinorXmlFeedApiCall(
            airportCodeParam: String,
            timeFromParam: Int?,
            timeToParam: Int?,
            directionParam: String?,
            lastUpdateParam: java.time.Instant?,
            serviceTypeParam: String?,
            codeshareParam: Boolean?
        ): String? {

            requestedAirports.add(airportCodeParam)
            return """
                  <airport name="$airportCodeParam">
                    <flights lastUpdate="2026-01-15T10:00:00Z">
                        <flight uniqueID="12345">
                            <flight_id>SK123</flight_id>
                            <schedule_time>2026-01-15T10:00:00Z</schedule_time>
                            <arr_dep>D</arr_dep>
                            <airport>TRD</airport>
                            <status code="A" time="2026-01-15T10:05:00Z"/>
                        </flight>
                    </flights>
                </airport>
            """.trimIndent()
        }
    }

    /**
     * Test to verify that fetchAndProcessAirports processes airports without network errors.
     */
    @Test
    fun `fetchAndProcessAirports should call API for every code in the file`() = runBlocking {
        val tempFile = File.createTempFile("test_airports", ".txt")
        tempFile.writeText("OSL\nBGO\nTRD")
        tempFile.deleteOnExit()

        val fakeApi = FakeAvinorApi()
        val service = AirportService(api = fakeApi)

        service.fetchAndProcessAirports(tempFile.absolutePath)

        assertEquals(3, fakeApi.requestedAirports.size, "Should have requested exactly 3 airports")

        assertTrue(fakeApi.requestedAirports.contains("OSL"), "Should have requested OSL")
        assertTrue(fakeApi.requestedAirports.contains("BGO"), "Should have requested BGO")
        assertTrue(fakeApi.requestedAirports.contains("TRD"), "Should have requested TRD")

        assertFalse(fakeApi.requestedAirports.contains("SVG"), "Should NOT have requested SVG")
    }
}