package routes.api

import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress

class Endpoint() {

    fun siriEtEndpoint(siriXml: String) {
        val server = HttpServer.create(InetSocketAddress(8080), 0)

        server.createContext("/siri") { exchange ->

            exchange.responseHeaders.add("Content-Type", "application/xml")
            exchange.sendResponseHeaders(200, siriXml.toByteArray().size.toLong())
            exchange.responseBody.use { it.write(siriXml.toByteArray()) }
        }

        server.start()
        println("Server running on port 8080")
    }
}