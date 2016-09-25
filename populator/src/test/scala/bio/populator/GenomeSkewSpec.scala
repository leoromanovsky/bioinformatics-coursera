package bio.populator

import bio.core.BioUnitSpec

class GenomeSkewSpec extends BioUnitSpec {
  describe("skew") {
    it("should have size 0") {
      val subject = GenomeSkew(NucleobaseSequence("CATGGGCATCGGCCATACGCC"))
      val result = subject.skew
      assert(result == Seq(0, -1, -1, -1, 0, 1, 2, 1, 1, 1, 0, 1, 2, 1, 0, 0, 0, 0, -1, 0, -1, -2))
    }

    it("is min skew") {
      val clz = GenomeSkew(NucleobaseSequence("TAAAGACTGCCGAGAGGCCAACACGAGTGCTAGAACGAGGGGCGTAAACGCGGGTCCGAT"))
      assert(clz.indexOfMinSkew == Seq(11, 24))
    }
  }
}