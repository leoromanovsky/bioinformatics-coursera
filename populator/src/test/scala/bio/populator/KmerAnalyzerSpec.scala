package bio.populator

import bio.core.BioUnitSpec

class KmerAnalyzerSpec extends BioUnitSpec {
  describe("patternCount") {
    it("should have size 0") {
      val subject = KmerAnalyzer(NucleobaseSequence(""))
      assert(subject.patternCount(NucleobaseSequence("")) == 0)
    }

    it("is simple count") {
      val subject =
        KmerAnalyzer(NucleobaseSequence("ACAACTATGCATACTATCGGGAACTATCCT"))
      assert(subject.patternCount(NucleobaseSequence("ACTAT")) == 3)
    }

    it("is a sliding count") {
      val subject = KmerAnalyzer(NucleobaseSequence("CGATATATCCATAG"))
      assert(subject.patternCount(NucleobaseSequence("ATA")) == 3)
    }
  }

  describe("mostFrequent") {
    it("is correct") {
      val subject = KmerAnalyzer(NucleobaseSequence("ACTGACTCCCACCCC"))
      val out = subject.mostFrequent(3).headOption
      assert(out.nonEmpty)
      assert(out.get == NucleobaseSequence("CCC"))
    }
  }

  describe("matchPattern") {
    it("is correct") {
      val subject = KmerAnalyzer(NucleobaseSequence("GATATATGCATATACTT"))
      val out = subject.matchPattern(NucleobaseSequence("ATAT"))
      assert(out == Seq(1, 3, 9))
    }
  }

  describe("clumb") {
    it("finds it") {
      val subject = KmerAnalyzer(NucleobaseSequence(
        "CGGACTCGACAGATGTGAAGAACGACAATGTGAAGACTCGACACGACAGAGTGAAGAGAAGAGGAAACATTGTAA"))
      val out = subject.clump(5, 50, 4)
      assert(
        out == Seq("CGACA", "GAAGA"))
    }
  }
}
