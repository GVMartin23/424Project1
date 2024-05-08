import scala.collection.parallel.CollectionConverters.*

trait POISort {
  private final val EARTH_RADIUS = 6371

  def euclideanDistance(pointOfInterest: PointOfInterest, pointVal: (Double, Double)): Double = {
    Math.sqrt(
      Math.pow(pointOfInterest.latitude - pointVal._1, 2.0)
        + Math.pow(pointOfInterest.longitude - pointVal._2, 2.0)
    )
  }
  def manhattanDistance(pointOfInterest: PointOfInterest, pointVal: (Double, Double)): Double = {
    Math.abs(pointOfInterest.latitude - pointVal._1) + Math.abs(pointOfInterest.longitude - pointVal._2)
  }
  def haversineDistance(pointOfInterest: PointOfInterest, point: (Double, Double)): Double = {
    def haversine(value: Double): Double = {
      Math.pow(Math.sin(value / 2), 2)
    }

    val differenceLat = Math.toRadians(pointOfInterest.latitude - point._1)
    val differenceLong = Math.toRadians(pointOfInterest.longitude - point._2)
    val a = haversine(differenceLat) + Math.cos(Math.toRadians(pointOfInterest.latitude)) * Math.cos(Math.toRadians(pointOfInterest.longitude)) * haversine(differenceLong)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    EARTH_RADIUS * c
  }
}

object POISortPar extends POISort {
  def sort(pointOfInterests: Vector[PointOfInterest], pointVal: (Double, Double), distanceFormula: (a: PointOfInterest, b: (Double, Double)) => Double): Vector[PointOfInterest] = {
    val result = pointOfInterests.zipWithIndex.par.map(item => (item._2, distanceFormula(item._1, pointVal))).seq.sortBy(_._2)
    result.slice(0, Math.min(10, result.size)).map(item => pointOfInterests(item._1))
  }
}

object POIShortSeq extends POISort {
  def sort(pointOfInterests: Vector[PointOfInterest], pointVal: (Double, Double), distanceFormula: (PointOfInterest, (Double, Double)) => Double): Vector[PointOfInterest] = {
    val result = pointOfInterests.sortBy(distanceFormula(_, pointVal))
    result.slice(0, Math.min(10, result.size))
  }
}