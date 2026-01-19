import com.sun.net.httpserver.HttpServer
import handler.AvinorScheduleXmlHandler
import routes.api.AvinorApiHandler
import siri.SiriETMapper
import siri.SiriETPublisher
import java.net.InetSocketAddress


fun siriEtEndpoint() {
    val server = HttpServer.create(InetSocketAddress(8080), 0)
    val siriMapper = SiriETMapper()
    val siriET = SiriETPublisher()
    val avinorApi = AvinorApiHandler()
    val AVXH = AvinorScheduleXmlHandler()

    var testOslData = avinorApi.avinorXmlFeedApiCall("OSL")
    val airport = AVXH.unmarshallXmlToAirport(testOslData ?: "")
    val siri = siriMapper.mapToSiri(airport, "OSL")


    server.createContext("/siri") { exchange ->
        val siriXml = siriET.toXml(siri)

        exchange.responseHeaders.add("Content-Type", "application/xml")
        exchange.sendResponseHeaders(200, siriXml.toByteArray().size.toLong())
        exchange.responseBody.use { it.write(siriXml.toByteArray()) }
    }

    server.start()
    println("Server running on port 8080")
}