import scala.collection.parallel.CollectionConverters.*
import scala.io.StdIn.readLine

@main def main():Unit={
  //Get user input until ":quit" is entered
  var query: String = ""
  var point: List[String] = List()
  while {
    println("Enter a point to search nearby format 'lat long' or (:quit) to quit")

    query = readLine(">")

    point = query.trim.split(' ').toList

    point != List(":quit")
  } do {
    try {
      val pointVal: (Double, Double) = (point.head.toDouble, point(1).toDouble)
      val radius: Double = readLine("radius>").trim.split(' ').head.toDouble //Don't care if there are too many inputs, only want first
      val edges: List[Int] = readLine("which polygons>").trim.split(' ').map(_.toInt).toList

      //Can use this in case we want multiple polygons of same center and radius
      //This returns a function that takes a number of edges and returns a polygon
      val shapeConstructor: Int => Polygon = Polygon(pointVal, radius)

      //TODO: Filter list of places by each polygon in list
      //TODO: Or you can just use the first polygon idc
      val shape: List[Polygon] = edges.map(shapeConstructor)
    } catch
      case e: NumberFormatException => println("Please input valid doubles")
      case i: IndexOutOfBoundsException => println("Make sure to provide enough inputs (need to inputs for 'lat' and 'long')")
  }
}