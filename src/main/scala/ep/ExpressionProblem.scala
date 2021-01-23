package ep

object Main extends App {
  import math._

  // The Expression Problem and my sources:
  // http://stackoverflow.com/questions/3596366/what-is-the-expression-problem
  // http://blog.ontoillogical.com/blog/2014/10/18/solving-the-expression-problem-in-clojure/
  // http://eli.thegreenplace.net/2016/the-expression-problem-and-its-solutions/
  // http://www.ibm.com/developerworks/library/j-clojure-protocols/


  // To begin demonstrating the problem, we first need some
  // "legacy code" with datastructures and functionality:


  // data structures ("shapes")

  //data Triangle = Triangle {a::Double,b::Double,c::Double}
  //data Square = Square {edge::Double}
  case class Triangle(a: Double, b: Double, c: Double)

  case class Square(edge: Double)

  // protocols
  // Protocols are optional and only required if we want to have a common type for our shapes
  // that represents some functionality like Areable.
  // These traits (Areable, ...) can be removed together with 'extends Areable' if we only
  // intend to add functionality, and not define type hierarchy. See examples below.

  trait Areable {
    /**
     * calculates the shape's area
     */
    def area: Double
  }

  trait SelfAware {
    /**
     * returns the name of the shape
     */
    def whoami: String
  }

  // implementations

  implicit class AreableTriangle(val x: Triangle) extends Areable {
    /**
     * use Heron's formula to calculate area
     */
    def area = {
      import x._
      val s = (a + b + c) / 2
      sqrt(s * (s - a) * (s - b) * (s - c))
    }
  }

  implicit class SelfAwareTriangle(val x: Triangle) extends SelfAware {
    def whoami = "Triangle"
  }

  implicit class AreableSquare(val x: Square) extends Areable {
    def area = x.edge * x.edge
  }

  implicit class SelfAwareSquare(val x: Square) extends SelfAware {
    def whoami = "Square"
  }

  // Solving the Expression Problem
  // 1. adding new functionality
  // 2. adding new datastructures (that play well with existing functionality)


  // 1.
  // a new protocol is invented: Perimeterable
  // can we add this new functionality without altering existing code ?
  // => yes we can !
  //

  trait Perimeterable {
    /**
     * calculates the perimeter of the shape
     */
    def perimeter: Double
  }

  implicit class PerimeterableTriangle(val x: Triangle) extends Perimeterable {
    def perimeter = x.a + x.b + x.c
  }

  implicit class PerimeterableSquare(val x: Square) extends Perimeterable {
    def perimeter = x.edge * 4
  }

  // 2.
  // a new shape is discovered: Circle
  // can we add a this new shape without altering existing code ?
  // => yes we can !
  //

  case class Circle(r: Double)

  implicit class AreableCircle(val x: Circle) extends Areable {
    def area = Pi * pow(x.r, 2)
  }

  implicit class SelfAwareCircle(val x: Circle) extends SelfAware {
    def whoami = "Circle"
  }

  implicit class PerimeterableCircle(val x: Circle) extends Perimeterable {
    def perimeter = x.r * 2
  }


  // "tests"
  println(Triangle(1, 1, 1).area)
  // > 0.4330127018922193
  println(Triangle(1, 1, 1).whoami)
  // > Triangle
  println(Triangle(1, 1, 1).perimeter)
  // > 3.0

  println(Square(2).area)
  // > 4.0
  println(Square(2).whoami)
  // > Square
  println(Square(2).perimeter)
  // > 8.0

  println(Circle(3).area)
  // > 28.274333882308138
  println(Circle(3).whoami)
  // > Circle
  println(Circle(3).perimeter)
  // > 6.0



  // Examples above show that functionality can be added to new datatypes,
  // and also that new datatypes can be added without breaking functionality.
  //
  // Code below shows that since we extend common Trait (Protocol), we can
  // treat all implementations as if they are subtypes of the same parent type,
  // i.e. they can be used as Areable, SelfAware or Perimeterable.
  def areaOf(x: Areable) = x.area

  println(areaOf(Triangle(1, 1, 1)))
  // > 0.4330127018922193
  println(areaOf(Square(2)))
  // > 4.0
  println(areaOf(Circle(3)))
  // > 28.274333882308138

  // analogously to 'areaOf':
  def whoamiOf(x: SelfAware) = x.whoami
  def perimeterOf(x:Perimeterable) = x.perimeter
}