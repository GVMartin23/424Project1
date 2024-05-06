import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class PointOfInterestSpec extends AnyFlatSpec with Matchers {
  "POI (0,0)" should "equal POI(0,0)" in {
    PointOfInterest("test", 0, 0) == PointOfInterest("test", 0, 0) should be (true)
  }

  "POI (0,0)" should "not equal POI(0,0)" in {
    PointOfInterest("test", 0, 0) == PointOfInterest("notTest", 0, 0) should be (false)
  }

  "POI (0,0)" should "not equal POI(1,0)" in {
    PointOfInterest("test", 0, 0) == PointOfInterest("test", 1, 0) should be (false)
  }

  "POI (0,0)" should "not equal POI(0, 1)" in {
    PointOfInterest("test", 0, 0) == PointOfInterest("test", 0, 1) should be (false)
  }

}