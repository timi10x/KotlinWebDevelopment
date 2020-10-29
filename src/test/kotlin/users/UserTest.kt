package users

import io.ktor.application.Application
import io.ktor.http.*
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import mainModule
import org.junit.Assert
import org.junit.Test
import kotlin.test.assertEquals

class UserTest {

    @Test
    fun `create user`(){
        withTestApplication(Application::mainModule){
            val call = handleRequest(HttpMethod.Post,"/users"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(listOf("name" to "daniel", "age" to "3").formUrlEncode())
            }

            Assert.assertEquals(HttpStatusCode.OK, call.response.status())
        }
    }
}