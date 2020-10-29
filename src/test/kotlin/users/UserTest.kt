package users

import asJson
import io.ktor.application.Application
import io.ktor.http.*
import io.ktor.http.HttpMethod.Companion.Delete
import io.ktor.server.testing.*
import mainModule
import org.eclipse.jetty.util.log.Log
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class UserTest {

    @Test
    fun `create user`() {
        withTestApplication(Application::mainModule) {
            val call = createUser("daniel", 500)

            assertEquals(HttpStatusCode.OK, call.response.status())
        }
    }

    @Test
    fun `get cat by ID`() {
        withTestApplication(Application::mainModule) {
            val createCall = createUser("timmy", 280)
            val id = createCall.response.content
            val afterCall = handleRequest(HttpMethod.Get, "/users/$id")

            assertEquals("""{"id":1,"name":"timmy","age":280} """.asJson(), afterCall.response.content?.asJson())
        }
    }

    @Test
    fun `get all users`() {
        withTestApplication(Application::mainModule) {
            val beforeCall = handleRequest(HttpMethod.Get, "/users")
            assertEquals("[]".asJson(), beforeCall.response.content?.asJson())

            createUser("Olatoye", 220)

            val afterCall = handleRequest(HttpMethod.Get, "/users")
            assertEquals("""[{"id":1,"name":"Olatoye","age":220}]""".asJson(), afterCall.response.content?.asJson())
        }
    }

    @Test
    fun `delete users by id`() {
        withTestApplication(Application::mainModule) {
            val createCall = createUser("timmy", 280)
            val id = createCall.response.content
            handleRequest(Delete, "/users/$id")

            val afterCall = handleRequest(HttpMethod.Get, "/users/$id")

            assertEquals(HttpStatusCode.NotFound, afterCall.response.status())
        }
    }

    @Before
    fun cleanup() {
        DB.connect()
        transaction {
            SchemaUtils.drop(Users)
        }
    }

    @Test
    fun `update user`() {
        withTestApplication(Application::mainModule) {
            val createCall = createUser("Olatoye", 220)
            val id = createCall.response.content

            handleRequest(HttpMethod.Put, "/users/$id") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(listOf("name" to "Olatoye", "age" to 9.toString()).formUrlEncode())
            }

            val afterUpdate = handleRequest(HttpMethod.Get, "/users/$id")

            assertEquals("""{"id":1,"name":"Olatoye","age":9}""".asJson(), afterUpdate.response.content?.asJson())

        }
    }

    private fun TestApplicationEngine.createUser(name: String, age: Int): TestApplicationCall {

        return handleRequest(HttpMethod.Post, "/users") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("name" to name, "age" to age.toString()).formUrlEncode())
        }
    }
}
