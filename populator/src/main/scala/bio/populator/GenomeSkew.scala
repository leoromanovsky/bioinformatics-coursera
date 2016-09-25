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
}

object GenomeSkew {
  def apply(seq: Seq[Nucleobase]): GenomeSkew = new GenomeSkew(seq)

  def main(args: Array[String]): Unit = {
    val input = NucleobaseSequence(Source.fromFile("data/test.txt").getLines.mkString)
    val clz = GenomeSkew(input)
    println(clz.indexOfMinSkew.mkString(" "))
  }
}
