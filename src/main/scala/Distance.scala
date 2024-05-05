  def findClosestToCenter(pointsOfInterest : List[PointOfInterest], pointVal: (Double, Double)): PointOfInterest= {
    pointsOfInterest.minBy(elem => Math.abs(
      Math.sqrt(Math.pow(elem.latitude - pointVal._1, 2)) +
        Math.sqrt(Math.pow(elem.longitude - pointVal._2, 2))
    ))
  }
