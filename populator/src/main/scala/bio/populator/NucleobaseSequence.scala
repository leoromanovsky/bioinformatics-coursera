package bio.populator

import bio.populator.Nucleobases.Nucleobase


object NucleobaseSequence {
  def reverseCompliment(seq: Seq[Nucleobase]): Seq[Nucleobase] = {
    seq.map(_.reverse).reverse
  }

  def apply(s: String): Seq[Nucleobase] = {
    s.map(Nucleobases.parseString(_))
  }

  def patternToNumber(pattern: Seq[Nucleobase]): Long = {
    if (pattern.isEmpty) {
      return 0
    }

    4 * patternToNumber(pattern.init) + encoding(pattern.last)
  }

  def numberToPattern(index: Long, k: Int): String = {
    if (k == 1) {
      return decoding(index.toInt).toString
    }

    val prefixIndex: Int = (index / 4).toInt
    val r: Int = (index % 4).toInt
    val symbol = decoding(r).toString
    val prefixPattern = numberToPattern(prefixIndex, k - 1)
    s"$prefixPattern$symbol"
  }

  private val encoding: Map[Nucleobase, Int] = Map(
    Nucleobases.Adenine -> 0,
    Nucleobases.Cytosine -> 1,
    Nucleobases.Guanine -> 2,
    Nucleobases.Thymine -> 3
  )

  private val decoding: Map[Int, Nucleobase] = Map(
    0 -> Nucleobases.Adenine,
    1 -> Nucleobases.Cytosine,
    2 -> Nucleobases.Guanine,
    3 -> Nucleobases.Thymine
  )
}
