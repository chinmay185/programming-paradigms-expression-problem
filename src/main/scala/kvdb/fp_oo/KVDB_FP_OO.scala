package kvdb.fp_oo

object KVDB_FP_OO extends App {

  import scala.collection.mutable.{HashMap => MutableHashMap}

  override def main(args: Array[String]): Unit = {
    val store = MutableHashMap[String, String]()

    // fist we ship this code
    withoutImplicits(store)
    implicits(store)

    // in the next release, we add new types and behaviour
    nextRelease(store)
  }

  def withoutImplicits(store: MutableHashMap[String, String]) = {
    assert(ExecutableSetCommand(SetCommand("key", "value")).execute(store) == "value")
    assert(ExecutableGetCommand(GetCommand("key")).execute(store) == "value")
    ExecutableDelCommand(DelCommand("key")).execute(store)
    assert(ExecutableGetCommand(GetCommand("key")).execute(store) == null)
  }

  def implicits(store: MutableHashMap[String, String]) = {
    assert(SetCommand("key", "value").execute(store) == "value")
    assert(GetCommand("key").execute(store) == "value")
    DelCommand("key").execute(store)
    assert(GetCommand("key").execute(store) == null)
  }

  case class SetCommand(key: String, value: String)
  case class GetCommand(key: String)
  case class DelCommand(key: String)

  trait Executable { def execute(store: MutableHashMap[String, String]): String }

  implicit class ExecutableSetCommand(val sc: SetCommand) extends Executable {
    override def execute(store: MutableHashMap[String, String]): String = {
      store.put(sc.key, sc.value);
      sc.value
    }
  }
  implicit class ExecutableGetCommand(val gc: GetCommand) extends Executable {
    override def execute(store: MutableHashMap[String, String]): String = { store.getOrElse(gc.key, null) }
  }
  implicit class ExecutableDelCommand(val dc: DelCommand) extends Executable {
    override def execute(store: MutableHashMap[String, String]): String = { store.remove(dc.key).orNull }
  }
  // ------------------------------------------------------------------------------------------------------------------ //
  // now we can easily add new behaviour like, stringify as well as new type, like incr command

  // adding stringify for all existing commands - Without modifying existing code
  trait Stringer { def stringify: String }

  implicit class SetCommandStringer(sc: SetCommand) extends Stringer {
    override def stringify: String = s"SET ${sc.key} ${sc.value}"
  }
  implicit class GetCommandStringer(gc: GetCommand) extends Stringer {
    override def stringify: String = s"GET ${gc.key}"
  }
  implicit class DelCommandStringer(dc: DelCommand) extends Stringer {
    override def stringify: String = s"DEL ${dc.key}"
  }

  // adding incr command execution and stringification - Without modifying existing code
  case class IncrCommand(key: String)

  implicit class ExecutableIncrCommand(ic: IncrCommand) extends Executable {
    override def execute(store: MutableHashMap[String, String]): String = {
      store.get(ic.key) match {
      case None => store.put(ic.key, "1"); "1"
      case Some(v) =>
        val incremented = (Integer.parseInt(v) + 1).toString
        store.put(ic.key, incremented);
        incremented
      }
    }
  }
  implicit class IncrCommandStringer(ic: IncrCommand) extends Stringer {
    override def stringify: String = s"INCR ${ic.key}"
  }

  def nextRelease(store: MutableHashMap[String, String]) = {
    println(IncrCommand("counter").execute(store))
    println(ExecutableIncrCommand(IncrCommand("counter")).execute(store))
    println(IncrCommand("counter").stringify)
  }

}