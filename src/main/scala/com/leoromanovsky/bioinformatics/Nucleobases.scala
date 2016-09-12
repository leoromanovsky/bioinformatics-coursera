package com.leoromanovsky.bioinformatics

object Nucleobases {
  sealed abstract class Nucleobase(symbol: String)
  case object Adenine extends Nucleobase("A")
  case object Thymine extends Nucleobase("T")
  case object Cytosine extends Nucleobase("C")
  case object Guanine extends Nucleobase("G")
}
