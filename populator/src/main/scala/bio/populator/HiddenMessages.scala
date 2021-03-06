package bio.populator

import bio.populator.Nucleobases.Nucleobase

import scala.io.Source

object HiddenMessages {
  val filename = "data/vibrio_cholerae.txt"
  lazy val genome = new LoadGenome(filename).genome

  def main(args: Array[String]): Unit = {
    //println(s"VibrioCholerae length: ${genome.length}")

    /*
    val input = Source.fromFile("data/test.txt").getLines.mkString.split(" ")
    val genome = input(0)
    val pattern = input(1)

    println(KMer.count(genome, pattern))
     */

    /*
    val g = "TACAATGATACAATGAGGGCCCGCGTCGCTCGTCGCTCACGCAATCACGCAATGTTAATGATCACGCAATCACGCAATGTTAATGATGGGCCCGGTTAATGATCGTCGCTCACGCAATTACAATGAGGGCCCGCACGCAATCACGCAATGGGCCCGGGGCCCGCGTCGCTGTTAATGATCGTCGCTCGTCGCTTACAATGAGGGCCCGTACAATGAGTTAATGATTACAATGAGGGCCCGGGGCCCGCGTCGCTCACGCAATCACGCAATGGGCCCGCACGCAATCGTCGCTCGTCGCTCGTCGCTCACGCAATGTTAATGATTACAATGATACAATGACGTCGCTTACAATGACACGCAATCACGCAATCGTCGCTTACAATGACACGCAATTACAATGACGTCGCTTACAATGAGGGCCCGTACAATGAGTTAATGATCGTCGCTCACGCAATTACAATGAGTTAATGATCACGCAATTACAATGACGTCGCTTACAATGATACAATGACGTCGCTCACGCAATGGGCCCGCGTCGCTGGGCCCGGTTAATGATCGTCGCTGTTAATGATTACAATGATACAATGAGGGCCCGCGTCGCTCGTCGCTCGTCGCTCGTCGCTCACGCAATCACGCAATCACGCAATGTTAATGATCACGCAATGTTAATGATTACAATGAGTTAATGATTACAATGACACGCAATGGGCCCGGGGCCCGGGGCCCGCACGCAATGTTAATGATTACAATGACACGCAATTACAATGACGTCGCTCACGCAATCACGCAATTACAATGACACGCAATGTTAATGATCACGCAATGGGCCCGTACAATGA"
    val f = 14
    KMer.mostFrequent(g, f).foreach(println)
     */

    /*
    println(
      NucleobaseSequence.patternToNumber(
        NucleobaseSequence("GCTGAGCATAGAAAA")))
*/

    //println(NucleobaseSequence.numberToPattern(6823, 10))

    //val input = NucleobaseSequence(Source.fromFile("data/e_coli.txt").getLines.mkString)
    val analyzer = KmerAnalyzer(NucleobaseSequence("CGGAGGACTCTAGGTAACGCTTATCAGGTCCATAGGACATTCA"))
    println(analyzer.mostFrequent(5))

    /*
    val input = Source.fromFile("data/vibrio_cholerae.txt").getLines.mkString
    val output = KMer.matchPattern(input, "CTTGATCAT").mkString(" ")
    println(output)
   */
  }
}

class LoadGenome(filename: String) {
  private val rawGenome = Source.fromFile(filename).getLines.mkString
  val genome: Seq[Nucleobase] = NucleobaseSequence(rawGenome)
}
