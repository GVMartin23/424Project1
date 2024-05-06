class PointOfInterest(val name: String, private val latitudeRadian: Double, private val longitudeRadian: Double) {
  def latitude: Double = Math.toDegrees(latitudeRadian)
  def longitude: Double = Math.toDegrees(longitudeRadian)
  override def toString: String = {
    s"${name} http://maps.google.com/maps?z=12&t=m&q=loc:${latitude}+${longitude}"
  }

  override def equals(that: Any): Boolean =
    that match {
      case that: PointOfInterest => that.name == this.name && that.latitudeRadian == this.latitudeRadian && that.longitudeRadian == this.longitudeRadian
      case _ => false
    }
}
