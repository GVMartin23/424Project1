import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class POIFilterSpec extends AnyFlatSpec with Matchers {
  "POI equivalent list from (0,0)" should "contain (0, 2pi), (0, -2pi), (pi, pi), (pi, -pi)" in {
    val poiList = POIFilterSeq.poiToList(PointOfInterest("test", 0, 0))
    poiList contains PointOfInterest("test", 0, 2*Math.PI) should be (true)
    poiList contains PointOfInterest("test", 0, -1*2*Math.PI) should be (true)
    poiList contains PointOfInterest("test", Math.PI, Math.PI) should be (true)
    poiList contains PointOfInterest("test", Math.PI, -1*Math.PI) should be (true)
  }
}
