package bio.populator

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

  def parseSequence(s: String): Seq[Nucleobase] = {
    s.map(parseString(_))
  }
}

class KMerAnalyzer(genome: String) {

  // How many times does `pattern` appear in `genome`
  def patternCount(pattern: String): Int = {
    val patternLength = pattern.length
    val genomeLength = genome.length
    patternLength < genomeLength match {
      case true => 0
      case false => genome.sliding(patternLength).count(_ == pattern)
    }
  }

  def mostFrequent(length: Int): String = {
    val frequencies = genome
      .sliding(length)
      .map { word =>
        (word, patternCount(word))
      }
      .toSeq
    val largestFreq: Int = frequencies.maxBy(_._2)._2
    frequencies.filter(_._2 == largestFreq).map(_._1).distinct.head
  }

  def reverseCompliment: String = {
    genome.map {
      case 'A' => 'T'
      case 'T' => 'A'
      case 'G' => 'C'
      case 'C' => 'G'
    }.mkString.reverse
  }

  /*
  All starting positions in Genome where Pattern appears as a substring.
   */
  def matchPattern(pattern: String): Seq[Int] = {
    genome
      .sliding(pattern.length)
      .zipWithIndex
      .filter(_._1 == pattern)
      .map(_._2)
      .toSeq
  }

  def clump(k: Int, l: Int, t: Int): Seq[String] = {
    genome
      .sliding(l)
      .map { window =>
        val miniAnalyze = new KMerAnalyzer(window)

        val m = miniAnalyze.mostFrequent(k)
        val c = miniAnalyze.patternCount(m)
        (m, c)
      }
      .filter(_._2 >= t)
      .map(_._1)
      .toSeq
      .distinct
  }
}
