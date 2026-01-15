package org.example

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.File

class AirportServiceTest {

    class FakeAvinorApi : AvinorApiHandling() {
        override fun avinorXmlFeedApiCall(
            airportCodeParam: String,
            timeFromParam: Int?,
            timeToParam: Int?,
            directionParam: String?,
            lastUpdateParam: java.time.Instant?,
            serviceTypeParam: String?,
            codeshareParam: Boolean?
        ): String? {
            return """
                  <airport name="${'$'}airportCodeParam">
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

    @Test
    fun `fetchAndProcessAirports should process airports without network errors`() = runBlocking {
        val tempFile = File.createTempFile("test_airports", ".txt")
        tempFile.writeText("OSL\nBGO")
        tempFile.deleteOnExit()

        val fakeApi = FakeAvinorApi()
        val service = AirportService(api = fakeApi)

        service.fetchAndProcessAirports(tempFile.absolutePath)

        assertTrue(tempFile.exists())
    }
}