object POIFilter {
  def filterPois(pois: List[PointOfInterest], shape: Polygon):List[PointOfInterest] = {
    //Take a Poi, map it to a list of equivalent poi's, then check if any of those are contained within the shape, if any are, keep shape
    pois.map(poiToList).filter(poiListContains(_, shape)).map(_.head)
  }
  
  def shapeContainsPoi(shape: Polygon, poi: PointOfInterest): Boolean = {
    shape.contains(poi.longitude, poi.latitude)
  }

  def poiListContains(poiList: List[PointOfInterest], shape: Polygon): Boolean = {
    poiList.map(shapeContainsPoi(shape, _)).reduce(_||_)
  }

  /**
   * Need to have a list pois that are equivalent to original.
   * These are useful when the shape drawn goes off the bounded [-180, 180] longitude and [-90, 90] latitude
   * Going "over" or "under" the poles provides weird wrap around that need accounted for
   * @param poi PointOfInterest to make equivalents of
   * @return List of POI's that are the same as original poi, but shifted
   */
  def poiToList(poi: PointOfInterest): List[PointOfInterest] = {
    //Making latitude [0,180] for easier up/down operations
    val latitudeAdjustmentDegrees = 90
    val positiveLat = poi.latitude + latitudeAdjustmentDegrees
    //Take latitude in positive, adjust it up by twice how far it is from latitude 180
    // 90 latitude raw becomes 180 positive, stays at 180.
    // -90 raw becomes 0 positive, moves to 360
    // 0 raw becomes 90 positive, moves to 180
    val aboveLatitudePositive = positiveLat + (2 * (180 - positiveLat))
    //Refit to [-90,90]
    val aboveLatitudeFinal = aboveLatitudePositive - latitudeAdjustmentDegrees

    val aboveLatitudeRadians = Math.toRadians(aboveLatitudeFinal)
    val belowLatitudeRadians = aboveLatitudeRadians * -1
    println(aboveLatitudeRadians)

    //Since we are going over the pole, our longitude now becomes offset 180 or -180
    //depending on if longitude is positive or negative, so we do both twice for full coverage
    val longitudeAdjustmentRadians = Math.toRadians(180)

    //Radians are private, need to convert to create
    val originalPOILatitudeRadians = Math.toRadians(poi.latitude)
    val originalPOILongitudeRadians = Math.toRadians(poi.longitude)

    val upRight = PointOfInterest(poi.name, aboveLatitudeRadians, originalPOILongitudeRadians + longitudeAdjustmentRadians)
    val upFarRight = PointOfInterest(poi.name, aboveLatitudeRadians, originalPOILongitudeRadians + (2 * longitudeAdjustmentRadians))
    val upLeft = PointOfInterest(poi.name, aboveLatitudeRadians, originalPOILongitudeRadians - longitudeAdjustmentRadians)
    val upFarLeft = PointOfInterest(poi.name, aboveLatitudeRadians, originalPOILongitudeRadians - (2 * longitudeAdjustmentRadians))

    val downRight = PointOfInterest(poi.name, belowLatitudeRadians, originalPOILongitudeRadians + longitudeAdjustmentRadians)
    val downFarRight = PointOfInterest(poi.name, belowLatitudeRadians, originalPOILongitudeRadians + (2 * longitudeAdjustmentRadians))
    val downLeft = PointOfInterest(poi.name, belowLatitudeRadians, originalPOILongitudeRadians - longitudeAdjustmentRadians)
    val downFarLeft = PointOfInterest(poi.name, belowLatitudeRadians, originalPOILongitudeRadians - (2 * longitudeAdjustmentRadians))

    //Going strictly left or right is easy since we can just shift all points left or right and wrap
    val right = PointOfInterest(poi.name, originalPOILatitudeRadians, originalPOILongitudeRadians + Math.PI*2)
    val left = PointOfInterest(poi.name, originalPOILatitudeRadians, originalPOILongitudeRadians - Math.PI*2)

    println(List(poi, right, left, upRight, upLeft, downRight, downLeft, upRight, upFarRight, upLeft, upFarLeft, downRight, downFarRight, downLeft, downFarLeft))

    //Original POI is left at head so easy to re-grab from list
    List(poi, right, left, upRight, upLeft, downRight, downLeft, upRight, upFarRight, upLeft, upFarLeft, downRight, downFarRight, downLeft, downFarLeft)
  }
}

@main def testMain():Unit = {
  POIFilter.poiToList(PointOfInterest("test", 0, 0))
}