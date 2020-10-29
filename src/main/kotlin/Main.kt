import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import users.*

fun main() {

    val port = System.getenv("PORT")?.toInt() ?: 8080

    val server = embeddedServer(Netty, port, module = Application::mainModule)
        .start(wait = true)
}

fun Application.mainModule() {

    DB.connect()

    transaction {
        //generating the table
        SchemaUtils.create(Users)
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        trace {
            application.log.debug(it.buildText())
        }
        get {
            call.respond(mapOf("Welcome" to "FullStack Crud"))
        }

        userRouter(UserServiceDb())
    }
}