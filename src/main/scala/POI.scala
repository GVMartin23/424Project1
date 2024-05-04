import scala.io.Source

class PointOfInterest(name: String, private val latitudeRadian: Double, private val longitudeRadian: Double) {
  def latitude: Double = latitudeRadian * (180 / Math.PI)
  def longitude: Double = longitudeRadian * (180 / Math.PI)
  override def toString: String = {
    s"${name} http://maps.google.com/maps?z=12&t=m&q=loc:${latitude}+${longitude}"
  }
}

object PointOfInterestIO {
  def loadRows(): List[PointOfInterest] = {
    val filename = "src/main/resources/poi_cleaned.csv"
    val source = Source.fromFile(filename)
    val lines = try source.getLines().toList finally source.close()
    lines.tail.map(line => {
      val lineParts = line.split(",")
      PointOfInterest(lineParts(0), lineParts(1).toDouble, lineParts(2).toDouble)
    })
  }
}