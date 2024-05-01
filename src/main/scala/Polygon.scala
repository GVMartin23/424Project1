import java.awt.geom.Path2D

class Polygon(edges: List[(Double, Double)]) {
  lazy val shape:Path2D = {
    val path: Path2D = new Path2D.Double()

    // Move to the first point in the polygon
    path.moveTo(edges.head._1, edges.head._2)
    edges.tail.foreach((lat, long) => path.lineTo(lat, long))
    // Close the path
    path.closePath()
    path
  }

  def contains(point: (Double, Double)): Boolean = {
    //TODO: Complete
    true
  }
}