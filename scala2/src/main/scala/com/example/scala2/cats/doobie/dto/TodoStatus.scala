package com.example.scala2.cats.doobie.dto

sealed abstract class TodoStatus(override val toString: String)

object TodoStatus {
  case object Waiting extends TodoStatus("waiting")
  case object Doing extends TodoStatus("doing")
  case object Done extends TodoStatus("done")

  val values =
    Seq(Waiting, Doing, Done).map(status => status.toString -> status).toMap
  def apply(statusStr: String): Option[TodoStatus] = values.get(statusStr)
}
