import scala.collection.parallel.CollectionConverters.*

trait POIFilter {
  def filterPois(pois: Vector[PointOfInterest], shape: Polygon) :Vector[PointOfInterest]

  def shapeContainsPoi(shape: Polygon, poi: PointOfInterest): Boolean = {
    shape.contains(poi.longitude, poi.latitude)
  }

  def poiListContains(poiList: Vector[PointOfInterest], shape: Polygon): Boolean = {
    poiList.map(shapeContainsPoi(shape, _)).reduce(_ || _)
  }

  /**
   * Need to have a list pois that are equivalent to original.
   * These are useful when the shape drawn goes off the bounded [-180, 180] longitude and [-90, 90] latitude
   * Going "over" or "under" the poles provides weird wrap around that need accounted for
   *
   * @param poi PointOfInterest to make equivalents of
   * @return List of POI's that are the same as original poi, but shifted
   */
  def poiToList(poi: PointOfInterest): Vector[PointOfInterest] = {
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

    //Radians are private, need to convert to create
    val originalPOILatitudeRadians = Math.toRadians(poi.latitude)
    val originalPOILongitudeRadians = Math.toRadians(poi.longitude)

    //Since we are going over the pole, our longitude now becomes offset 180 or -180
    //depending on if longitude is positive or negative, negative must go to positive vice versa
    val longitudeAdjustmentRadians = Math.toRadians(180) * (if originalPOILongitudeRadians > 0 then -1 else 1)

    val up = PointOfInterest(poi.name, aboveLatitudeRadians, originalPOILongitudeRadians + longitudeAdjustmentRadians)
    val down = PointOfInterest(poi.name, belowLatitudeRadians, originalPOILongitudeRadians + longitudeAdjustmentRadians)

    val twoPI = Math.PI * 2
    //Going strictly left or right is easy since we can just shift all points left or right and wrap
    val right = PointOfInterest(poi.name, originalPOILatitudeRadians, originalPOILongitudeRadians + Math.PI * 2)
    val left = PointOfInterest(poi.name, originalPOILatitudeRadians, originalPOILongitudeRadians - Math.PI * 2)


    val upRight = PointOfInterest(poi.name, aboveLatitudeRadians, Math.toRadians(up.longitude) + twoPI)
    val upLeft = PointOfInterest(poi.name, aboveLatitudeRadians, Math.toRadians(up.longitude) - twoPI)
    val downRight = PointOfInterest(poi.name, belowLatitudeRadians, Math.toRadians(down.longitude) + twoPI)
    val downLeft = PointOfInterest(poi.name, belowLatitudeRadians, Math.toRadians(down.longitude) - twoPI)

    //Original POI is left at head so easy to re-grab from list
    Vector(poi, right, left, up, down, upRight, upLeft, downRight, downLeft, upRight, upLeft, downLeft)
  }
}

object POIFilterSeq extends POIFilter {
  def filterPois(pois: Vector[PointOfInterest], shape: Polygon): Vector[PointOfInterest] = {
    //Take a Poi, map it to a list of equivalent poi's, then check if any of those are contained within the shape, if any are, keep shape
    pois.map(poiToList).filter(item => poiListContains(item, shape)).map(_.head)
  }
}

object POIFilterPar extends POIFilter {
  def filterPois(pois: Vector[PointOfInterest], shape: Polygon): Vector[PointOfInterest] = {
    //Take a Poi, map it to a list of equivalent poi's, then check if any of those are contained within the shape, if any are, keep shape
    pois.par.map(poiToList).filter(item => poiListContains(item, shape)).map(_.head).seq.toVector
  }
}