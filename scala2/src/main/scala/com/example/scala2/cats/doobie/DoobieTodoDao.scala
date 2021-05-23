package com.example.scala2.cats.doobie

import cats.data.NonEmptyList
import cats.effect.Bracket
import cats.implicits._
import doobie.Fragment
import doobie.implicits._
import doobie.util.fragments.{in, whereAndOpt}
import doobie.util.{Get, Read}
import doobie.util.transactor.Transactor
import com.example.scala2.cats.doobie.dto._
import doobie.util.log.LogHandler
import org.slf4j.LoggerFactory

class DoobieTodoDao[F[_]](transactor: Transactor[F])(implicit
    bracket: Bracket[F, Throwable]
) {

  implicit private val todoStatusGet: Get[TodoStatus] =
    Get[String].map(statusStr =>
      TodoStatus
        .apply(statusStr)
        .getOrElse(throw new MatchError(s"$statusStr is not TodoStatus"))
    )
  implicit private val todoDtoRead: Read[TodoDto] =
    Read[(TodoId, TodoTitle, TodoStatus)].map { case (id, title, status) =>
      TodoDto(id, title, status)
    }

  def findAll(): F[Seq[TodoDto]] =
    sql"""
         select todo_id, todo_title, todo_status
         from sample_db.todo
         """.query[TodoDto].to[Seq].transact(transactor)

  def findBy(id: TodoId): F[Option[TodoDto]] =
    sql"""
         select todo_id, todo_title, todo_status
         from sample_db.todo
         where todo_id = $id
       """.query[TodoDto].option.transact(transactor)

  // NOTE: `sql"""xxx"""`内で変数を埋め込む時, サニタイズしてくれる.
  def saveAndReturnKey(dto: NewTodoDto): F[TodoId] =
    sql"""
         insert into sample_db.todo
         (todo_title, todo_status)
         values
         (${dto.title}, ${dto.status.toString})
       """.update
      .withUniqueGeneratedKeys[TodoId]("todo_id")
      .transact(transactor)

  def update(dto: TodoDto): F[Unit] =
    sql"""
         update sample_db.todo
         set todo_title = ${dto.title},
             todo_status = ${dto.status.toString}
         where todo_id = ${dto.id}
       """.update.run.transact(transactor) *> ().pure[F]

  def deleteBy(id: TodoId): F[Unit] =
    sql"""
         delete from sample_db.todo
         where todo_id = $id
       """.update.run.transact(transactor) *> ().pure[F]
}
