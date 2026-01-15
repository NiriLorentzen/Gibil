import org.example.AvinorApiHandler
import java.io.File

class AirlineNameHandler(private val cacheFile: String = "airlines.json") {
    private val airlines = mutableMapOf<String, String>()

    init {
        loadCache()
    }

    private fun fetchAirlineXml(): String {
        val url = "https://asrv.avinor.no/airlineNames/v1.0"
        val avinorApiHandler = AvinorApiHandler()
        val result = avinorApiHandler.apiCall(url)
        if (result != null) {
            return result
        }
        return "Error"
    }

    fun update() {
        val xmlData = fetchAirlineXml()

        airlines.clear()

        // Simple regex to extract code and name from XML
        val pattern = """code="([^"]+)"\s+name="([^"]+)"""".toRegex()
        pattern.findAll(xmlData).forEach { match ->
            val code = match.groupValues[1]
            val name = match.groupValues[2]
            airlines[code] = name
        }

        saveCache()
    }

    fun getName(code: String): String? = airlines[code]

    fun isValid(code: String): Boolean = airlines.containsKey(code)

    private fun saveCache() {
        val json = airlines.entries.joinToString(",\n  ", "{\n  ", "\n}") {
            """"${it.key}": "${it.value}""""
        }
        File(cacheFile).writeText(json)
    }

    private fun loadCache() {
        val file = File(cacheFile)
        if (!file.exists()){
            update()
            return
        }

        val json = file.readText()
        val pattern = """"([^"]+)":\s*"([^"]+)"""".toRegex()
        pattern.findAll(json).forEach { match ->
            airlines[match.groupValues[1]] = match.groupValues[2]
        }
    }
}

