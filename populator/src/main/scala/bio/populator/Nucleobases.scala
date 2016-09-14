package bio.populator

import bio.populator.Nucleobases.Nucleobase

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

case class Genome(nucleobases: Seq[Nucleobase]) {}

object Genome {}

class KMerAnalyzer(nucleobaseSequence: Seq[Nucleobase]) {

  /**
    * How many times does `pattern` appear in `nucleobaseSequence`
    */
  def patternCount(pattern: Seq[Nucleobase]): Int = {
    val patternLength = pattern.length
    patternLength match {
      case 0 => 0
      case 1 => 0
      case _ =>
        nucleobaseSequence.sliding(patternLength).count(_ == pattern)
    }
  }

  def frequencies(length: Int): Map[Long, Int] = {
    nucleobaseSequence
      .sliding(length)
      .foldLeft(Map[Long, Int]().withDefaultValue(0)) { (h, a) =>
        val s = NucleobaseSequence.patternToNumber(a)
        h.updated(s, h(s) + 1)
      }
  }

  /**
    * Returns the most frequently occurring sequences.
    */
  def mostFrequent(length: Int): Seq[Seq[Nucleobase]] = {
    val frequencyMap = frequencies(length)

    val largestFreq: Int = frequencyMap.maxBy(_._2)._2
    frequencyMap.find {
      case (k, count) =>
        count == largestFreq
    }.map(_._1).toSeq.distinct.map { a =>
      //NucleobaseSequence.decodeHash(a, length)
      Seq()
    }
  }

  /**
    * All starting positions in Genome where Pattern appears as a substring.
    */
  def matchPattern(pattern: Seq[Nucleobase]): Seq[Int] = {
    nucleobaseSequence
      .sliding(pattern.length)
      .zipWithIndex
      .filter(_._1 == pattern)
      .map(_._2)
      .toSeq
  }

  /**
    * k - k-mer size
    * L - window size
    * t - frequency of interest
    */
  def clump(k: Int, l: Int, t: Int): Seq[String] = {
    nucleobaseSequence
      .sliding(l)
      .flatMap { window =>
        val miniAnalyze = new KMerAnalyzer(window)
        miniAnalyze.frequencies(k).filter(_._2 >= t).map { w =>
          //NucleobaseSequence.decodeHash(w._1, k)
          Seq()
        }
      }
      .toSeq
      .distinct
      .map(_.mkString)
      .sorted
  }
}
