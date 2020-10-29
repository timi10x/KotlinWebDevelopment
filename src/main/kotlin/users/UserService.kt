package users

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

interface UserService {
    suspend fun create(name: String, age: Int?): Int

    suspend fun getAllUsers(): List<User>

    suspend fun findById(id: Int): User?

    //suspend fun deleteUser()

    suspend fun deleteUserById(id: Int)

    suspend fun updateUser(id: Int, name: String, age: Int?)
}

class UserServiceDb : UserService {
    override suspend fun create(name: String, age: Int?): Int {
        val id = transaction {
            Users.insertAndGetId { user ->
                user[Users.name] = name
                if (age != null) {
                    user[Users.age] = age
                }
            }
        }
        return id.value
    }

    override suspend fun getAllUsers(): List<User> {
        return transaction {
            Users.selectAll().map { row ->
                row.asUser()
            }
        }
    }

    override suspend fun findById(id: Int): User? {
        val row = transaction {
            addLogger(StdOutSqlLogger)
            Users.select {
                Users.id eq id
            }.firstOrNull()
        }
        return row?.asUser()
    }

    /*override suspend fun deleteUser(){
        transaction {
            Users.deleteAll()
        }
    }*/

    override suspend fun deleteUserById(id: Int) {
        transaction {
            Users.deleteWhere {
                Users.id eq id
            }
        }
    }

    override suspend fun updateUser(id: Int, name: String, age: Int?) {
        transaction {
            Users.update({
                Users.id eq id
            }) { user ->
                user[Users.name] = name
                if (age != null) {
                    user[Users.age] = age
                }

            }
        }
    }

}

private fun ResultRow.asUser() = User(
    //in the context of this method,
    //"this[]" refers to the db row
    this[Users.id].value,
    this[Users.name],
    this[Users.age]
)

data class User(
    val id: Int,
    val name: String,
    val age: Int
) {


}