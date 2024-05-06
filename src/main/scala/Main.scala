import scala.io.StdIn.readLine

@main def main():Unit={
  val POI1 = PointOfInterest("POI1", 0.2, 0.2)
  val POI2 = PointOfInterest("POI2", .9, .9)
  val POI3 = PointOfInterest("POI3", 0.6, 0.6)

  val poiList = List(POI1, POI2, POI3)
  val testPoint = (0.5, 0.5)
  println(findClosestToCenter(poiList, testPoint).toString)
  //Get user input until ":quit" is entered
  var query: String = ""
  var point: List[String] = List()
  val pois = PointOfInterestIO.loadRows()
//  println(pois)
  while {
    println(
    """
    Welcome to
       _           _                  _        _         _
      | |___ _ _ _|_|___    ___ ___ _| |   ___| |___ ___| |_
      | | -_| | | | |_ -|  | .'|   | . |  |  _| | .'|  _| '_|
      |_|___|_____|_|___|  |__,|_|_|___|  |___|_|__,|_| |_,_|
    """)
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
      val shapes: List[Polygon] = edges.map(shapeConstructor)
      
      val resultsPerShape = shapes.map(POIFilter.filterPois(pois, _))

    } catch
      case e: NumberFormatException => println("Please input valid doubles")
      case i: IndexOutOfBoundsException => println("Make sure to provide enough inputs (need to inputs for 'lat' and 'long')")
  }
}