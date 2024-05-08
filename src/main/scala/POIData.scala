import scala.collection.SeqView
import scala.io.Source

object POIData {
  def loadRows(percentData: Double = 100): SeqView[PointOfInterest] = {
    val filename = "src/main/resources/poi_cleaned.csv"
    val source = Source.fromFile(filename)
    val lines = try source.getLines().toList finally source.close()
    val numRows = lines.tail.length
    lines.tail.view.take((numRows / (100 - percentData)).toInt).map(line => {
      val lineParts = line.split(",")
      PointOfInterest(lineParts(0), lineParts(1).toDouble, lineParts(2).toDouble)
    })
  }
}