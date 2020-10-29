import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object User: IntIdTable(){
    val name = varchar("name", 20).uniqueIndex()
    val age = integer("age").default(0)
}

