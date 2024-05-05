
  def filterPois(pois: List[PointOfInterest], shape: Polygon):List[PointOfInterest] = {
    //Take a Poi, map it to a list of equivalent poi's, then check if any of those are contained within the shape, if any are, keep shape
    pois.map(poiToList).filter((poiList) => poiList.map((poi) => shapeContainsPoi(shape, poi)).reduce(_||_)).map(_.head)
  }
  
  def shapeContainsPoi(shape: Polygon, poi: PointOfInterest): Boolean = {
    shape.contains((Math.toDegrees(poi.longitudeRadian), Math.toDegrees(poi.latitudeRadian)))
  }
  
  def poiToList(poi: PointOfInterest): List[PointOfInterest] = {
    //Need to have a list of 9 pois that are equivalent to original. 
    // (I.E. 0, -95 = (180 - 85)
    val above = PointOfInterest(poi.name, 180 - poi.latitudeRadian, poi.longitudeRadian)
    val below = PointOfInterest(poi.name, 180 + poi.latitudeRadian, poi.longitudeRadian)
    val right = PointOfInterest(poi.name, poi.latitudeRadian, poi.longitudeRadian + 360)
    val left = PointOfInterest(poi.name, poi.latitudeRadian, poi.longitudeRadian - 360)
    val upRight = PointOfInterest(poi.name, above.latitudeRadian, right.longitudeRadian)
    val upLeft = PointOfInterest(poi.name, above.latitudeRadian, left.longitudeRadian)
    val downRight = PointOfInterest(poi.name, below.latitudeRadian, right.longitudeRadian)
    val downLeft = PointOfInterest(poi.name, below.latitudeRadian, left.longitudeRadian)
    
    List(poi, above, below, right, left, upRight, upLeft, downRight, downLeft)
  }
