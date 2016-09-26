package bio.populator

import java.io.Serializable

object Nucleobases {
  sealed abstract class Nucleobase(symbol: String) extends Serializable {
    def reverse: Nucleobase

    override def toString: String = symbol
  }

  case object Adenine extends Nucleobase("A") {
    override def reverse: Nucleobase = Thymine
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

  val parseString: Map[String, Nucleobase] = Map(
    "A" -> Adenine,
    "T" -> Thymine,
    "C" -> Cytosine,
    "G" -> Guanine
  )

  def otherBases(s: Char): Seq[Char] = {
    s match {
      case 'A' => Seq('T', 'C', 'G')
      case 'T' => Seq('A', 'C', 'G')
      case 'C' => Seq('A', 'T', 'G')
      case 'G' => Seq('A', 'T', 'C')
    }
  }
}
