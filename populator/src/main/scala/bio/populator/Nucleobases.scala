package bio.populator

object Nucleobases {
  sealed abstract class Nucleobase(symbol: String) {

    def reverse: Nucleobase

    override def toString: String = symbol
  }

  case object Adenine extends Nucleobase("A") {
    override def reverse = Thymine
  }

  case object Thymine extends Nucleobase("T") {
    override def reverse: Nucleobase = Adenine
  }

  case object Cytosine extends Nucleobase("C") {
    override def reverse: Nucleobase = Guanine
  }

  case object Guanine extends Nucleobase("G") {
    override def reverse: Nucleobase = Cytosine
  }

  val parseString: Map[Char, Nucleobase] = Map(
    'A' -> Adenine,
    'T' -> Thymine,
    'C' -> Cytosine,
    'G' -> Guanine
  )
}
