package kvdb.fp.v1

import scala.collection.mutable.{HashMap => MutableHashMap}

object KVDB_V1 extends App {
  override def main(args: Array[String]): Unit = {
    // OO style invocation
    assert(Store.execute(SetCommand("key", "value")) == "value")
    assert(Store.execute(GetCommand("key")) == "value")
    Store.execute(DelCommand("key"))
    assert(Store.execute(GetCommand("key")) == null)

    // function invocation
    assert(execute(SetCommand("key", "value")) == "value")
    assert(execute(GetCommand("key")) == "value")
    execute(DelCommand("key"))
    assert(execute(GetCommand("key")) == null)
  }
}

trait Command
case class SetCommand(key: String, value: String) extends Command
case class GetCommand(key: String) extends Command
case class DelCommand(key: String) extends Command

object Store {

  val store = new MutableHashMap[String, String]()

  def execute(c: Command): String = {
    c match {
      case SetCommand(k, v) => store.put(k, v); v
      case GetCommand(k) => store.getOrElse(k, null)
      case DelCommand(k) => store.remove(k).orNull
    }
  }
}

// this is scala's way of defining a function as an object but callable as a first class function
object execute extends (Command => String) {
  override def apply(c: Command): String = Store.execute(c)
}