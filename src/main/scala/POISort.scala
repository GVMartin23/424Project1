object POISort {
  def sort(pointOfInterests: Vector[PointOfInterest], pointVal: (Double, Double)): Vector[PointOfInterest] = {
    val result = pointOfInterests.sortWith(distanceFromCenter(_, pointVal) < distanceFromCenter(_, pointVal))
    result.slice(0, Math.min(10, result.size))
  }

  private def distanceFromCenter(pointOfInterest: PointOfInterest, pointVal: (Double, Double)): Double = {
    Math.sqrt(
      Math.pow(pointOfInterest.latitude - pointVal._1, 2.0)
        + Math.pow(pointOfInterest.longitude - pointVal._2, 2.0)
    )
  }
}

object POISortSeq {
  def sort(pointOfInterests: Vector[PointOfInterest], pointVal: (Double, Double)): Vector[PointOfInterest] = {
    val result = pointOfInterests.sortWith(distanceFromCenter(_, pointVal) < distanceFromCenter(_, pointVal))
    result.slice(0, Math.min(10, result.size))
  }

  private def distanceFromCenter(pointOfInterest: PointOfInterest, pointVal: (Double, Double)): Double = {
    Math.sqrt(
      Math.pow(pointOfInterest.latitude - pointVal._1, 2.0)
        + Math.pow(pointOfInterest.longitude - pointVal._2, 2.0)
    )
  }
}

