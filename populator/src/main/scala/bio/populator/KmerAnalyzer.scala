package bio.populator

import bio.populator.Nucleobases.Nucleobase
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.mllib._
import org.apache.spark.mllib.rdd.RDDFunctions._
import org.apache.spark.rdd.RDD

import scala.io.Source

object KmerAnalyzer {
  def apply(nucleobaseSequence: Seq[Nucleobase]): KmerAnalyzer =
    new KmerAnalyzer(nucleobaseSequence)
}

case class EncodedNucleobaseSequence(underlying: Long)

class KmerAnalyzer(nucleobaseSequence: Seq[Nucleobase]) {

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
      NucleobaseSequence(NucleobaseSequence.numberToPattern(a, length))
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
    val firstSlice = nucleobaseSequence.slice(0, l)
    val freqMap = KmerAnalyzer(firstSlice).frequencies(k)
    val runningFreqMap = collection.mutable
        .Map[Long, Int]()
        .withDefaultValue(0) ++= freqMap
    val clumps = scala.collection.mutable.Map[Long, Int]().withDefaultValue(0)

    runningFreqMap.foreach {
      case (key, v) =>
        if (v >= t) {
          clumps(key) = clumps(key) + 1
        }
    }

    for (i <- 1 until nucleobaseSequence.size - l) {
      val firstPattern = nucleobaseSequence.slice(i - 1, i - 1 + k)
      val firstIndex = NucleobaseSequence.patternToNumber(firstPattern)
      runningFreqMap(firstIndex) = runningFreqMap(firstIndex) - 1

      val lastPattern = nucleobaseSequence.slice(i + l - k, i + l)
      val lastIndex = NucleobaseSequence.patternToNumber(lastPattern)
      runningFreqMap(lastIndex) = runningFreqMap(lastIndex) + 1

      if (runningFreqMap(lastIndex) >= t) {
        clumps(lastIndex) = 1
      }
    }

    clumps
      .filter(_._2 == 1)
      .map {
        case (key, _) =>
          NucleobaseSequence.numberToPattern(key, k)
      }
      .toSeq
  }
}

class KmerAnalyzerSpark(data: RDD[String])(implicit sc: SparkContext) {
  def frequenciesSpark(length: Int): RDD[(Long, Int)] = {
    data
      .sliding(length)
      .map { seq =>
        val converted = seq.map(s => Nucleobases.parseString(s))
        (NucleobaseSequence.patternToNumber(converted), 1)
      }
      .reduceByKey(_ + _)
  }
}

object KmerAnalyzerSpark {
  def main(args: Array[String]) {
    val conf =
      new SparkConf().setAppName("Simple Application").setMaster("local[8]")
    implicit val sc = new SparkContext(conf)

    val startTime = System.currentTimeMillis()
    val rddInput = sc.textFile("data/e_coli.txt")
    val analyzer = new KmerAnalyzerSpark(rddInput)
    analyzer.frequenciesSpark(20).collect()
    val endTime = System.currentTimeMillis()
    println("Spark: ", endTime - startTime)

    val startTime2 = System.currentTimeMillis()
    val input = NucleobaseSequence(
      Source.fromFile("data/e_coli.txt").getLines.mkString)
    val a2 = KmerAnalyzer(input)
    a2.frequencies(20)
    val endTime2 = System.currentTimeMillis()
    println("Normal: ", endTime2 - startTime2)
  }
}
