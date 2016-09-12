package bio

class KMerSpec extends BioUnitSpec {
  describe("count") {
    it("should have size 0") {
      assert(KMer.count("", "") == 0)
    }

    it("is simple count") {
      assert(KMer.count("ACAACTATGCATACTATCGGGAACTATCCT", "ACTAT") == 3)
    }

    it("is a sliding count") {
      assert(KMer.count("CGATATATCCATAG", "ATA") == 3)
    }
  }

  describe("wordFrequences") {
    it("is correct") {
      assert(KMer.mostFrequent("ACTGACTCCCACCCC", 3) == Seq("CCC"))
    }
  }
}
