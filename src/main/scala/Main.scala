import scala.io.StdIn.readLine

@main def main():Unit={
  //Get user input until ":quit" is entered
  var query: String = ""
  var point: List[String] = List()
  val pois = POIData.loadRows()

  println(
    """
  Welcome to
     _           _                  _        _         _
    | |___ _ _ _|_|___    ___ ___ _| |   ___| |___ ___| |_
    | | -_| | | | |_ -|  | .'|   | . |  |  _| | .'|  _| '_|
    |_|___|_____|_|___|  |__,|_|_|___|  |___|_|__,|_| |_,_|
  """)
  println("Enter a point to search nearby format 'lat long' or (:quit) to quit")
  while {
    query = readLine("> ")
    point = query.trim.split(' ').toList
    point != List(":quit")
  } do {
    try {
      val pointVal: (Double, Double) = (point(1).toDouble, point.head.toDouble)
      val radius: Double = readLine("radius> ").trim.split(' ').head.toDouble //Don't care if there are too many inputs, only want first
      val edges: List[Int] = readLine("which polygons> ").trim.split(' ').map(_.toInt).toList

      //Can use this in case we want multiple polygons of same center and radius
      //This returns a function that takes a number of edges and returns a polygon
      val shapeConstructor: Int => Polygon = Polygon(pointVal, radius)

      //TODO: Filter list of places by each polygon in list
      //TODO: Or you can just use the first polygon idc
      val shapes: List[Polygon] = edges.map(shapeConstructor)
      val filterStartTime = System.currentTimeMillis()
      val resultsPerShape = POIFilterSeq.filterPois(pois.toVector, shapes)
      val filterTime = System.currentTimeMillis() - filterStartTime

      if (resultsPerShape.isEmpty) {
        println("No points found within the polygon. Please try again using different parameters")
      } else {
        val sortStartTime = System.currentTimeMillis()
        val result = POISortPar.sort(resultsPerShape, pointVal, POISortPar.manhattanDistance)
        val sortTime = System.currentTimeMillis() - sortStartTime
        val subsetSize = Math.min(10, result.size)
        println(s"Top ${subsetSize} Points closest to the center of the shape:")
        result.slice(0, subsetSize).zipWithIndex.foreach(item => println(s"${item._2 + 1}. ${item._1}"))
        println(s"${result.size} points within the shape. Took ${sortTime + filterTime}ms\n to calculate.")
      }
    } catch
      case e: NumberFormatException => println("Please input valid doubles")
      case i: IndexOutOfBoundsException => println("Make sure to provide enough inputs (need to inputs for 'lat' and 'long')")
  }
}