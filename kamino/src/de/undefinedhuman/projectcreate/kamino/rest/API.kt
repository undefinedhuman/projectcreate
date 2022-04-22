package de.undefinedhuman.projectcreate.kamino.rest

import com.google.gson.JsonArray
import de.undefinedhuman.projectcreate.kamino.database.Database
import de.undefinedhuman.projectcreate.kamino.query.QueryEndpoint
import de.undefinedhuman.projectcreate.kamino.query.QueryUtils
import de.undefinedhuman.projectcreate.kamino.utils.Decompressor
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class API(private val database: Database, private val decompressor: Decompressor) {

    private val server: ApplicationEngine;

    init {
        server = embeddedServer(Netty, port = 8080) {
            install(ContentNegotiation) {
                gson()
            }

            routing {
                get("/v1/eventTypes") {
                    call.respond(QueryUtils.getQueryTypesJson())
                }
                post("/v1/events") {
                    val array = call.receive<JsonArray>()
                    val response = QueryEndpoint.parseRequest(database, array, decompressor, false)
                    if (response == null)
                        call.respond(HttpStatusCode.NotFound, "{\"error\":\"Error while querying events!\"}")
                    call.respond(HttpStatusCode.OK, response)
                }
            }
        }
    }

    fun start() {
        server.start(false)
    }

    fun stop() {
        server.stop(30 * 1000L, 0)
    }
}