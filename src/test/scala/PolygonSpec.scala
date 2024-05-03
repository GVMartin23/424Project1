import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class PolygonSpec extends AnyFlatSpec with Matchers {
  "Polygon with 4 sides, radius of 5, and centered at 0" should "contain (0,0)" in {
    Polygon((0,0), 5)(4).contains((0,0)) should be (true)
  }

  "Polygon with 4 sides, radius of 5, and centered at 0" should "contain (2.45,2.45)" in {
    Polygon((0, 0), 5)(4).contains((2.45, 2.45)) should be(true)
  }

  "Polygon with 4 sides, radius of 5, and centered at 0" should "contain (-1.1,-1.1)" in {
    Polygon((0, 0), 5)(4).contains((-1.1, -1.1)) should be(true)
  }

  "Polygon with 4 sides, radius of 5, and centered at 0" should "not contain (4,3)" in {
    Polygon((0, 0), 5)(4).contains((4, 3)) should be(false)
  }

}
