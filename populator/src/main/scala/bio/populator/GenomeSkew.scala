package bio.populator

import bio.populator.Nucleobases.Nucleobase

import scala.io.Source

class GenomeSkew(seq: Seq[Nucleobase]) {

  /**
    * Calculate the skew and return as a sequence of integers.
    *
    * If a nucleotide is G, then skew increases.
    * If a nucleotide is C, then is decreases.
    * A and T have no effect on skew.
    */
  def skew: Seq[Int] = {
    val skews = seq.map {
      case Nucleobases.Guanine => 1
      case Nucleobases.Cytosine => -1
      case _ => 0
    }
    Seq(0) ++ skews.drop(1).scanLeft(skews.head) {
      case (r, c) => r + c
    }
  }

  def indexOfMinSkew: Seq[Int] = {
    val skews = skew
    skews.zipWithIndex.filter(_._1 == skews.min).map(_._2)
  }

  def hammingDistance(s1: String, s2: String): Int =
    s1.zip(s2).count(c => c._1 != c._2)

  def approxPatternMatch(pattern: Seq[Nucleobase],
                         maxDistance: Int): Seq[Int] = {
    seq
      .sliding(pattern.size)
      .zipWithIndex
      .filter {
        case (slide, index) =>
          hammingDistance(slide.mkString(""), pattern.mkString("")) <= maxDistance
      }
      .map(_._2)
      .toSeq
  }

  def approxPatternMatchCount(pattern: Seq[Nucleobase], maxDistance: Int) =
    approxPatternMatch(pattern, maxDistance).size

  // Generate the 1-neigborhood of Pattern
  def immediateNeighbors(pattern: String): Set[String] = {
    pattern.zipWithIndex.flatMap { case(char, index) =>
      Nucleobases.otherBases(char).flatMap { symbol =>
        Some(s"${pattern.take(index)}${symbol}${pattern.substring(index + 1, pattern.size)}")
      }
    }.toSet.+(pattern)
  }

  def neighbors(pattern: String, d: Int): Set[String] = {
    if (d <= 0) {
      return Set(pattern)
    }

    if (pattern.isEmpty) {
      return Set("ATCG")
    }

    val neighborhood: Set[String] = Set()

    val suffix = pattern.substring(1, pattern.length)
    println("suffix", suffix)

    val suffixNeighbors = neighbors(suffix, d)
    println("suffixNeighbors", suffixNeighbors)

    suffixNeighbors.foreach { neighbor =>
      println("neighbor", neighbor)

      if (hammingDistance(suffix, neighbor) < d) {
        Seq('A', 'C', 'T', 'G').foreach { char =>
          neighborhood.+(s"$char$neighbor")
        }
      } else {
        neighborhood.+(s"${pattern.head}${neighbor}")
      }
    }

    println("neigh", neighborhood)

    neighborhood
  }
}

object GenomeSkew {
  def apply(seq: Seq[Nucleobase]): GenomeSkew = new GenomeSkew(seq)

  def main(args: Array[String]): Unit = {
    println(GenomeSkew(Seq()).immediateNeighbors("AA"))

    println(GenomeSkew(Seq()).neighbors("AA", 1))

    /*
    val input = NucleobaseSequence(Source.fromFile("data/test.txt").getLines.mkString)
    val clz = GenomeSkew(input)
    println(clz.indexOfMinSkew.mkString(" "))

       println(GenomeSkew(Seq())
      .hammingDistance("CTTGAAGTGGACCTCTAGTTCCTCTACAAAGAACAGGTTGACCTGTCGCGAAG",
        "ATGCCTTACCTAGATGCAATGACGGACGTATTCCTTTTGCCTCAACGGCTCCT"))
     */


    /*
    val input = NucleobaseSequence(
      Source.fromFile("data/test.txt").getLines.mkString)
    println(
      GenomeSkew(input)
        .approxPatternMatch(NucleobaseSequence("AATATCAGAATA"), 4)
        .mkString(" "))


        println(
      GenomeSkew(NucleobaseSequence("CGTGACAGTGTATGGGCATCTTT"))
        .approxPatternMatchCount(NucleobaseSequence("TGT"), 1))
     */

  }
}
