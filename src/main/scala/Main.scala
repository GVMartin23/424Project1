import scala.collection.parallel.CollectionConverters.*

@main def main_funct():Unit={
  (1 until 100000).par.map(println(_))
  println("testing")
}