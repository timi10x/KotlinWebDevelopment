package users

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Database

object Users : IntIdTable() {
    val name = varchar("name", 20).uniqueIndex()
    val age = integer("age").default(0)
}



