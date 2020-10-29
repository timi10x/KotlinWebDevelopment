package users

import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction

interface UserService {
    suspend fun create(name: String, age: Int?): Int
}

class UserServiceDb: UserService{
    override suspend fun create(name: String, age: Int?):Int{
        val id = transaction {
            User.insertAndGetId {
                user ->
                user[User.name] = name
                if (age != null){
                    user[User.age] = age
                }
            }
        }
        return id.value
    }

}