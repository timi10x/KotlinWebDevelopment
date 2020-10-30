import org.jetbrains.exposed.sql.Database

object DB {
    private val host = System.getenv("DB_HOST")
    private val port = System.getenv("DB_PORT")
    private val databaseName = System.getenv("DB_NAME")
    private val databaseUser = System.getenv("DB_USER")
    private val databasePassword = System.getenv("DB_PASSWORD")

    fun connect(): Database {
        return Database.connect(
            "jdbc:postgresql://$host:$port/$databaseName",
            driver = "org.postgresql.Driver", user = databaseUser, password = databasePassword
        )
    }


}