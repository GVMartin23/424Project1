  def findClosestToCenter(pointsOfInterest : List[PointOfInterest], pointVal: (Double, Double)): PointOfInterest= {
    pointsOfInterest.minBy(elem =>
      Math.sqrt(
        Math.pow(elem.latitude - pointVal._1, 2.0)
        + Math.pow(elem.longitude - pointVal._2, 2.0)
      )
    )
  }
