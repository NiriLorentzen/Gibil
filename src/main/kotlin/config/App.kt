package config

import handler.AvinorScheduleXmlHandler
import routes.api.AvinorApiHandler
import routes.api.Endpoint
import siri.SiriETMapper
import siri.SiriETPublisher
import siri.validator.XsdValidator
import java.time.Clock

class App {
    val avinorApi = AvinorApiHandler()
    val avxh = AvinorScheduleXmlHandler()
    val endpoint = Endpoint()
    val siriMapper = SiriETMapper()
    val siriPublisher = SiriETPublisher()
    val xsdValidator = XsdValidator()
    val clock = Clock.systemUTC()
}