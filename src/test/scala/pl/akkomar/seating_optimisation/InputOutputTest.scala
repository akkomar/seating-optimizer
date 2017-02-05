package pl.akkomar.seating_optimisation

import org.scalatest.{FlatSpec, Matchers}

class InputReaderTest extends FlatSpec with Matchers {

  "Input Reader" should "properly read sample input file" in {
    val parsedInput = InputReader.read("src/test/resources/input.txt")

    parsedInput shouldEqual ExampleTestData.exampleInput
  }
}

class OutputWriterTest extends FlatSpec with Matchers {

  import ExampleTestData.intToSome

  "Output writer" should "properly parse output to string" in {
    val output = ExampleTestData.exampleOutput

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

object ExampleTestData {
  implicit def intToSome(i: Int): Option[Int] = Some(i)

  val exampleInput = Input(
    PlaneDimensions(4, 4),
    Seq(
      PassengerGroup(Seq(Passenger(1, windowPreferred = true), Passenger(2), Passenger(3))),
      PassengerGroup(Seq(Passenger(4), Passenger(5), Passenger(6), Passenger(7))),
      PassengerGroup(Seq(Passenger(8))),
      PassengerGroup(Seq(Passenger(9), Passenger(10), Passenger(11, windowPreferred = true))),
      PassengerGroup(Seq(Passenger(12, windowPreferred = true))),
      PassengerGroup(Seq(Passenger(13), Passenger(14))),
      PassengerGroup(Seq(Passenger(15), Passenger(16)))
    ))

  val exampleOutput = Output(
    Array(
      Array[Option[Int]](1, 2, 3, 8),
      Array[Option[Int]](4, 5, 6, 7),
      Array[Option[Int]](11, 9, 10, 12),
      Array[Option[Int]](13, 14, 15, 16)
    ),
    100)
}