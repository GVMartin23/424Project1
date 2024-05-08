object POISort {
  private final val EARTH_RADIUS = 6371;

  def sort(pointOfInterests: Vector[PointOfInterest], pointVal: (Double, Double), distanceFormula: (a: PointOfInterest, b: (Double, Double)) => Double): Vector[PointOfInterest] = {
    val result = pointOfInterests.sortBy(distanceFormula(_, pointVal))
    result.slice(0, Math.min(10, result.size))
  }

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
      def haversine(value: Double):Double = {
        Math.pow(Math.sin(value / 2), 2)
      }
      val differenceLat = Math.toRadians(pointOfInterest.latitude - point._1)
      val differenceLong = Math.toRadians(pointOfInterest.longitude - point._2)
      val a = haversine(differenceLat) + Math.cos(Math.toRadians(pointOfInterest.latitude)) * Math.cos(Math.toRadians(pointOfInterest.longitude)) * haversine(differenceLong)
      val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
      EARTH_RADIUS * c
  }
}
