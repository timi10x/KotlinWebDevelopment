package users

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveParameters
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.userRouter(userService: UserService){
    route("/users"){
        post {
            with(call){
                val parameters = receiveParameters()
                val name = requireNotNull(parameters["name"])
                val age = parameters["age"]?.toInt()

                userService.create(name, age)
                call.response.status(HttpStatusCode.Created)
            }
        }
    }
}