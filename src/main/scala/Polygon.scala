import java.awt.geom.{Path2D, Point2D}

class Polygon(edges: List[(Double, Double)]) {
  private lazy val shape: Path2D = {
    val path: Path2D = new Path2D.Double()

    // Move to the first point in the polygon
    path.moveTo(edges.head._1, edges.head._2)
    edges.tail.foreach((long, lat) => path.lineTo(long, lat))
    // Close the path
    path.closePath()
    path
  }

  def contains(point: (Double, Double)): Boolean = {
    shape.contains(new Point2D.Double(point._1, point._2))
  }
}

object Polygon {
  def apply(center: (Double, Double), radius:Double)(numEdges: Int): Polygon = {
    //Starting point (directly north of center)
    val northPoint = (center._1, center._2 + radius)

    if numEdges < 3 then throw IllegalArgumentException("Need more edges to form a polygon")
    //Find how much to rotate for each point
    val rotation = 360.toDouble / numEdges

    new Polygon((0 until numEdges).view.map(_*rotation).map(generatePoint(_, northPoint, center)).toList)
  }

  private def generatePoint(rotationDegrees: Double, northPoint:(Double, Double), center: (Double, Double)): (Double, Double) = {
    val rads = Math.toRadians(rotationDegrees)
    (center._1 + ((northPoint._1 - center._1) * Math.cos(rads)) - ((northPoint._2-center._2) * Math.sin(rads)),
      center._2 + ((northPoint._1 - center._1) * Math.sin(rads)) + ((northPoint._2-center._2) * Math.cos(rads))
    )
  }
}