package bio.core

import org.scalatest._

abstract class BioUnitSpec extends FunSpec with Matchers with
  OptionValues with Inside with Inspectors
