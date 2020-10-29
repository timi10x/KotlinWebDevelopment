package users

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.userRouter(userService: UserService){
    route("/users"){
        post {
            with(call){
                val parameters = receiveParameters()
                val name = requireNotNull(parameters["name"])
                val age = parameters["age"]?.toInt()

                val userId = userService.create(name, age)
                respond(HttpStatusCode.OK, userId)
            }
        }

        get("/{id}"){
            with(call){
                val id = requireNotNull(parameters["id"]).toInt()
                val user = userService.findById(id)

                if (user == null){
                    respond(HttpStatusCode.NotFound)
                }else{
                    respond(user)
                }
            }
        }

        get{
            call.respond(userService.getAllUsers())
        }

        /*delete ("/delete"){
            with(call){
                respond(HttpStatusCode.OK, userService.deleteUser())

            }
        }*/

        delete("/{id}"){
            with(call){
                val id = requireNotNull(parameters["id"]).toInt()
                val user = userService.deleteUserById(id)

            }

        }

        put("/{id}"){
            with(call){
                val id = requireNotNull(parameters["id"]).toInt()
                val parameters = receiveParameters()
                val name = requireNotNull(parameters["name"])
                val age = parameters["age"]?.toInt()

                userService.updateUser(id, name, age)
            }
        }
    }
}