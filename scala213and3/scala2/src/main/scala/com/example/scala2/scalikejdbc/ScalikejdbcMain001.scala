package com.example.scala2.scalikejdbc

import cats.effect.IO
import io.circe.Decoder
import io.circe.config.parser
import io.circe.generic.semiauto.deriveDecoder
import scalikejdbc._

/** ref. http://scalikejdbc.org/
  */
object ScalikejdbcMain001 extends App {
  case class DatabaseConfig(
      driver: String,
      url: String,
      user: String,
      password: String,
      connectionPoolSize: Int
  )
  implicit val dataBaseDecoder: Decoder[DatabaseConfig] = deriveDecoder
  val databaseConfig = parser.decodePathF[IO, DatabaseConfig]("database").unsafeRunSync()

  // scalikejdbcの設定
  Class.forName(databaseConfig.driver)
  ConnectionPool.singleton(databaseConfig.url, databaseConfig.user, databaseConfig.password)
  implicit val session = AutoSession

  val todoHashMaps: List[Map[String, Any]] = sql"select * from todo".map(_.toMap).list.apply()
  println(todoHashMaps)

  case class Todo(id: Int, title: String, status: String)
  object Todo extends SQLSyntaxSupport[Todo] {
    override val tableName = "todo"
    def of(rs: WrappedResultSet) = Todo(rs.int("todo_id"), rs.string("todo_title"), rs.string("todo_status"))
  }

  def findAllTodo: List[Todo] = sql"select * from todo".map(rs => Todo.of(rs)).list().apply()
  println(findAllTodo)

  val result = sql"insert into todo (todo_title, todo_status) values ('title4', 'waiting')".update().apply()
  println(s"insert result: $result")
  println(findAllTodo)

  sql"delete from todo where todo_id = 4".update().apply()
  println(findAllTodo)

  val t = Todo.syntax("t")
  val todoId1 = 1
  // TODO: カラム名が違うとうまくいかないかもしれないのでカスタムマッピング的なものを調べる.
  val maybeTodo1 = withSQL(select.from(Todo.as(t)).where.eq(t.id, todoId1)).map(Todo.of).single().apply()
  println(maybeTodo1)

}
