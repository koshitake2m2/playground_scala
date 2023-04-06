package com.example.scala2.cats.doobie

import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.example.scala2.cats.doobie.dto._
import io.circe.config.parser

object DoobieMain001 extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    (for {
      databaseConfig <- Resource.liftF(
        parser.decodePathF[IO, DatabaseConfig]("database")
      )
      transactor <- databaseConfig.transactor[IO]
      todoDao = new DoobieTodoDao[IO](transactor)
    } yield todoDao)
      .use { todoDao =>
        (for {
          _ <- IO()
          _ = println(s"\n${"#" * 20}\n")

          // 全件取得
          dtosBefore <- todoDao.findAll()
          _ = println(dtosBefore)

          // 新規追加
          newTodoDto =
            NewTodoDto(
              TodoTitle("new title"),
              TodoStatus.Waiting
            )
          targetId <- todoDao.saveAndReturnKey(newTodoDto)
          _ = println(s"new todo_id: $targetId")
          maybeCreatedTodoDto <- todoDao.findBy(targetId)
          _ = println(s"createdTodo: $maybeCreatedTodoDto")

          // 更新
          updatingTodoDto = TodoDto(
            targetId,
            TodoTitle("updated title"),
            TodoStatus.Doing
          )
          _ <- todoDao.update(updatingTodoDto)
          maybeUpdatedTodoDto <- todoDao.findBy(targetId)
          _ = println(s"updatedTodo: $maybeUpdatedTodoDto")

          // 削除
          _ <- todoDao.deleteBy(targetId)
          maybeDeletedTodoDto <- todoDao.findBy(targetId)
          _ = println(s"deletedTodo: $maybeDeletedTodoDto")

          // 全件取得
          dtosAfter <- todoDao.findAll()

          _ = println(dtosAfter)
          _ = println(s"\n${"#" * 20}\n")
        } yield ()) *> IO.pure()
      }
      .as(ExitCode.Success)
}
