package bio.populator

import bio.core.BioUnitSpec

class NucleobaseSequenceSpec extends BioUnitSpec {
  describe("reverseCompliment") {
    it("is produces a correct result") {
      assert(NucleobaseSequence.reverseCompliment(
        NucleobaseSequence("AAAACCCGGT")) == NucleobaseSequence("ACCGGGTTTT"))
    }
  }
}
