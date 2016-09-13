package bio.populator

import bio.core.BioUnitSpec

class KMerAnalyzerSpec extends BioUnitSpec {
  describe("patternCount") {
    it("should have size 0") {
      val subject = new KMerAnalyzer("")
      assert(subject.patternCount("") == 0)
    }

    it("is simple count") {
      val subject = new KMerAnalyzer("ACAACTATGCATACTATCGGGAACTATCCT")
      assert(subject.patternCount("ACTAT") == 3)
    }

    it("is a sliding count") {
      val subject = new KMerAnalyzer("CGATATATCCATAG")
      assert(subject.patternCount("ATA") == 3)
    }
  }

  describe("mostFrequent") {
    it("is correct") {
      val subject = new KMerAnalyzer("ACTGACTCCCACCCC")
      assert(subject.mostFrequent(3) == "CCC")
    }
  }

  describe("reverseCompliment") {
    it("is correct") {
      val subject = new KMerAnalyzer("AAAACCCGGT")
      assert(subject.reverseCompliment == "ACCGGGTTTT")
    }
  }

  describe("matchPattern") {
    it("is correct") {
      val subject = new KMerAnalyzer("AAAACCCGGT")
      val out = subject.matchPattern("ATAT").mkString(",")
      assert(out == "1,3,9")
    }
  }

  describe("clumb") {
    it("finds it") {
      val subject = new KMerAnalyzer(
        "CGGACTCGACAGATGTGAAGAACGACAATGTGAAGACTCGACACGACAGAGTGAAGAGAAGAGGAAACATTGTAA")
      val out = subject.clump(5, 50, 4)
      assert(out == Seq("CGACA", "GAAGA"))
    }
  }
}
