package bio.populator

import bio.populator.Nucleobases.Nucleobase

object Nucleobases {
  sealed abstract class Nucleobase(symbol: String) {
    def reverse: Nucleobase

    override def toString: String = symbol
  }

  case object Adenine extends Nucleobase("A") {
    def reverse = Thymine
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

case class NucleobaseSequence(underlying: Seq[Nucleobase])
    extends scala.collection.Seq[Nucleobase] {
  def reverseCompliment: NucleobaseSequence = {
    NucleobaseSequence(underlying.map(_.reverse).reverse)
  }

  override def length: Int = underlying.length

  override def iterator: Iterator[Nucleobase] = underlying.iterator

  override def apply(idx: Int): Nucleobase = underlying.apply(idx)
}

object NucleobaseSequence {
  def apply(s: String): NucleobaseSequence = {
    NucleobaseSequence(s.map(Nucleobases.parseString(_)))
  }
}

case class Genome(nucleobases: NucleobaseSequence) {}

object Genome {}

class KMerAnalyzer(nucleobaseSequence: NucleobaseSequence) {

  // How many times does `pattern` appear in `genome`
  def patternCount(pattern: Seq[Nucleobase]): Int = {
    val patternLength = pattern.length
    //val genomeLength = nucleobaseSequence.length
    patternLength match {
      case 0 => 0
      case 1 => 0
      case _ =>
        nucleobaseSequence.sliding(patternLength).count(_ == pattern)
    }
  }

  def mostFrequent(length: Int): Seq[NucleobaseSequence] = {
    val frequencies = nucleobaseSequence
      .sliding(length)
      .map { word =>
        (word, patternCount(word))
      }
      .toSeq
    val largestFreq: Int = frequencies.maxBy(_._2)._2
    frequencies
      .filter(_._2 == largestFreq)
      .map(_._1)
      .distinct
      .map(a => NucleobaseSequence(a))
  }

  /*
  All starting positions in Genome where Pattern appears as a substring.
   */
  def matchPattern(pattern: NucleobaseSequence): Seq[Int] = {
    nucleobaseSequence
      .sliding(pattern.length)
      .zipWithIndex
      .filter(_._1 == pattern)
      .map(_._2)
      .toSeq
  }

  def clump(k: Int, l: Int, t: Int): Seq[NucleobaseSequence] = {
    nucleobaseSequence
      .sliding(l)
      .map { window =>
        val miniAnalyze = new KMerAnalyzer(NucleobaseSequence(window))

        val m = miniAnalyze.mostFrequent(k).head
        val c = miniAnalyze.patternCount(m)
        (m, c)
      }
      .filter(_._2 >= t)
      .map(_._1)
      .toSeq
      .distinct
  }
}
