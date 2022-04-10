package de.undefinedhuman.projectcreate.kamino.rest

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val server = embeddedServer(Netty, port = 8080) {
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
    }
}

fun startServer() {
    server.start(wait = true)
}

fun stopServer() {
    server.stop(30 * 1000L, 0)
}