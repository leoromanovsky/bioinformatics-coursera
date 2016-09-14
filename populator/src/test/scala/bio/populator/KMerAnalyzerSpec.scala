package bio.populator

import bio.core.BioUnitSpec

class KMerAnalyzerSpec extends BioUnitSpec {
  describe("patternCount") {
    it("should have size 0") {
      val subject = new KMerAnalyzer(NucleobaseSequence(""))
      assert(subject.patternCount(NucleobaseSequence("")) == 0)
    }

    it("is simple count") {
      val subject =
        new KMerAnalyzer(NucleobaseSequence("ACAACTATGCATACTATCGGGAACTATCCT"))
      assert(subject.patternCount(NucleobaseSequence("ACTAT")) == 3)
    }

    it("is a sliding count") {
      val subject = new KMerAnalyzer(NucleobaseSequence("CGATATATCCATAG"))
      assert(subject.patternCount(NucleobaseSequence("ATA")) == 3)
    }
  }

  describe("mostFrequent") {
    it("is correct") {
      val subject = new KMerAnalyzer(NucleobaseSequence("ACTGACTCCCACCCC"))
      assert(subject.mostFrequent(3).head == NucleobaseSequence("CCC"))
    }
  }

  describe("reverseCompliment") {
    it("is correct") {
      val seq = NucleobaseSequence("AAAACCCGGT")
      assert(seq.reverseCompliment == NucleobaseSequence("ACCGGGTTTT"))
    }
  }

  describe("matchPattern") {
    it("is correct") {
      val subject = new KMerAnalyzer(NucleobaseSequence("GATATATGCATATACTT"))
      val out = subject.matchPattern(NucleobaseSequence("ATAT"))
      assert(out == Seq(1, 3, 9))
    }
  }

  describe("clumb") {
    it("finds it") {
      val subject = new KMerAnalyzer(NucleobaseSequence(
        "CGGACTCGACAGATGTGAAGAACGACAATGTGAAGACTCGACACGACAGAGTGAAGAGAAGAGGAAACATTGTAA"))
      val out = subject.clump(5, 50, 4)
      assert(
        out == Seq(NucleobaseSequence("CGACA"), NucleobaseSequence("GAAGA")))
    }
  }
}
