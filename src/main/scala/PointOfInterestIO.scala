import scala.io.Source

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