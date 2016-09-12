package bio

object Nucleobases {
  sealed abstract class Nucleobase(symbol: String)
  case object Adenine extends Nucleobase("A")
  case object Thymine extends Nucleobase("T")
  case object Cytosine extends Nucleobase("C")
  case object Guanine extends Nucleobase("G")

  val parseString: Map[String, Nucleobase] = Map(
    "A" -> Adenine,
    "T" -> Thymine,
    "C" -> Cytosine,
    "G" -> Guanine
  )
}

object KMer {
  /**
    *
    * @param code
    * @param pattern
    * @return
    */
  def count(code: String, pattern: String): Int = {
    val patternLength = pattern.length
    patternLength match {
      case 0 => 0
      case 1 => 0
      case _ => code.sliding(patternLength).count(_ == pattern)
    }
  }
}
