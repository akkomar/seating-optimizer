package pl.akkomar.seating_optimisation

import org.scalatest.{FlatSpec, Matchers}

class InputReaderTest extends FlatSpec with Matchers {

  "Input Reader" should "properly read sample input file" in {
    val parsedInput = InputReader.read("src/test/resources/input.txt")

    parsedInput shouldEqual Input(PlaneDimensions(4, 4), Seq())
  }
}

class OutputWriterTest extends FlatSpec with Matchers {

  "Output writer" should "properly parse output to string" in {
    val output = Output(
      Array(
        Array[Int](1, 2, 3, 8),
        Array[Int](4, 5, 6, 7),
        Array[Int](11, 9, 10, 12),
        Array[Int](13, 14, 15, 16)
      ),
      100)

    val outputString = OutputWriter.toString(output)

    val expectedOutputString =
      """1 2 3 8
        |4 5 6 7
        |11 9 10 12
        |13 14 15 16
        |100%""".stripMargin

    outputString shouldEqual expectedOutputString
  }
}