package kvdb.fp.v2

import kvdb.fp.v1.{Command, DelCommand, GetCommand, SetCommand}

import scala.collection.mutable.{HashMap => MutableHashMap}

object KVDB_V2 extends App {
  override def main(args: Array[String]): Unit = {
    // OO style invocation
    assert(Kvdb.stringify(SetCommand("key", "value")) == "SET key value")
    assert(Kvdb.stringify(GetCommand("key")) == "GET key")
    assert(Kvdb.stringify(DelCommand("key")) == "DEL key")

    // function invocation
    assert(stringify(SetCommand("key", "value")) == "SET key value")
    assert(stringify(GetCommand("key")) == "GET key")
    assert(stringify(DelCommand("key")) == "DEL key")
  }
}

object Kvdb {
  def stringify(c: Command): String = {
    c match {
      case SetCommand(k, v) => s"SET ${k} ${v}"
      case GetCommand(k) => s"GET ${k}"
      case DelCommand(k) => s"DEL ${k}"
    }
  }
}

case class IncrCommand(key: String) extends Command{}

// this is scala's way of defining a function as an object but callable as a first class function
// https://stackoverflow.com/questions/18548814/can-i-define-and-use-functions-outside-classes-and-objects-in-scala
object stringify extends (Command => String) {
  override def apply(c: Command): String = Kvdb.stringify(c)
}