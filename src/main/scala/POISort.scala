import scala.collection.parallel.CollectionConverters.*

trait POISort {
  private final val EARTH_RADIUS = 6371

  def haversineDistance(pointOfInterest: PointOfInterest, point: (Double, Double)): Double = {
    def haversine(value: Double): Double = {
      Math.pow(Math.sin(value / 2), 2)
    }

    val differenceLat = Math.toRadians(pointOfInterest.latitude - point._2)
    val differenceLong = Math.toRadians(pointOfInterest.longitude - point._1)
    val a = haversine(differenceLat) + Math.cos(Math.toRadians(pointOfInterest.latitude)) * Math.cos(Math.toRadians(pointOfInterest.longitude)) * haversine(differenceLong)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    EARTH_RADIUS * c
  }

  def equirectangular(pointOfInterest: PointOfInterest, point: (Double, Double)): Double = {
    val a = Math.abs(pointOfInterest.longitude) - Math.abs(point._1)
    val x = Math.toRadians(a) * Math.cos(Math.toRadians(pointOfInterest.latitude + point._2) / 2)
    val y = Math.toRadians(pointOfInterest.latitude - point._2)
    Math.sqrt(x * x + y * y) * EARTH_RADIUS
  }
}

object POISortPar extends POISort {
  def sort(pointOfInterests: Vector[PointOfInterest], pointVal: (Double, Double), distanceFormula: (PointOfInterest, (Double, Double)) => Double): Vector[(PointOfInterest, Double)] = {
    pointOfInterests.zipWithIndex.par.map(item => (item._2, distanceFormula(item._1, pointVal))).seq.sortBy(_._2).map(item => (pointOfInterests(item._1), item._2))
  }
}

object POIShortSeq extends POISort {
  def sort(pointOfInterests: Vector[PointOfInterest], pointVal: (Double, Double), distanceFormula: (PointOfInterest, (Double, Double)) => Double): Vector[(PointOfInterest, Double)] = {
    pointOfInterests.zipWithIndex.map(item => (item._2, distanceFormula(item._1, pointVal))).seq.sortBy(_._2).map(item => (pointOfInterests(item._1), item._2))
  }
}