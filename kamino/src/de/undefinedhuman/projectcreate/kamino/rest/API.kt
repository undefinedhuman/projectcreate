package de.undefinedhuman.projectcreate.kamino.rest

import de.undefinedhuman.projectcreate.kamino.query.QueryUtils
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val server = embeddedServer(Netty, port = 8080) {
    install(ContentNegotiation) {
        gson()
    }

    routing {
        get("/") {
            val uri = call.request.uri
            call.respondText("Request uri: $uri")
        }
        get("/eventTypes") {
            call.respond(QueryUtils.getQueryTypesJson())
        }
    }
}

fun start() {
    server.start(false)
}

fun stop() {
    server.stop(30 * 1000L, 0)
}