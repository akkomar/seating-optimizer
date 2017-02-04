package pl.akkomar.seating_optimisation

import org.scalatest.{FlatSpec, Matchers}

class InputReaderTest extends FlatSpec with Matchers {

  "Input Reader" should "properly read sample input file" in {
    val parsedInput = InputReader.read("src/test/resources/input.txt")

    parsedInput shouldEqual Input(PlaneDimensions(4, 4), Seq())
  }
}

class OutputWriterTest extends FlatSpec with Matchers {

  implicit def intToSome(i: Int): Option[Int] = Some(i)

  "Output writer" should "properly parse output to string" in {
    val output = Output(
      Array(
        Array[Option[Int]](1, 2, 3, 8),
        Array[Option[Int]](4, 5, 6, 7),
        Array[Option[Int]](11, 9, 10, 12),
        Array[Option[Int]](13, 14, 15, 16)
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

  it should "properly parse output with empty seats" in {
    val output = Output(
      Array(
        Array[Option[Int]](1, 2, 3, None),
        Array[Option[Int]](4, None, None, None)
      ),
      100)

    val outputString = OutputWriter.toString(output)

    val expectedOutputString =
      """1 2 3
        |4
        |100%""".stripMargin

    outputString shouldEqual expectedOutputString
  }
}