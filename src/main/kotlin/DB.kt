import org.jetbrains.exposed.sql.Database

object DB {
    private val host = "localhost"
    private val port = 5555
    private val databaseName = "fullstack_db"
    private val databaseUser = "fullstack_user"
    private val databasePassword = "fullstack123"

    fun connect(): Database {
        return Database.connect(
            "jdbc:postgresql://$host:$port/$databaseName",
            driver = "org.postgresql.Driver", user = databaseUser, password = databasePassword
        )
    }


}