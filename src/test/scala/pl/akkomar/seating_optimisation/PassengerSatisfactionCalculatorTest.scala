package pl.akkomar.seating_optimisation

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class PassengerSatisfactionCalculatorTest extends FlatSpec with Matchers with TableDrivenPropertyChecks {

  val calculator = PassengerSatisfactionCalculator

  val input = Input(
    PlaneDimensions(4, 1),
    Seq(
      PassengerGroup(Seq(Passenger(1, true), Passenger(2))),
      PassengerGroup(Seq(Passenger(3)))))

  val testExamples = Table[Array[Array[Option[Int]]], Int](
    ("seatMap", "expected satisfaction percent"),
    (Array(Array(Some(1), Some(2), Some(3), None)), 100),
    (Array(Array(Some(2), Some(1), Some(3), None)), 66),
    (Array(Array(Some(3), None, None, None)), 33)

  )

  "Passenger satisfaction calculator" should "return correct satisfaction calculation" in {
    forAll(testExamples) { (seatMap: Array[Array[Option[Int]]], expectedPercent: Int) =>
      calculator.calculatePassengerSatisfactionPercent(input, seatMap) shouldEqual expectedPercent
    }
  }
}
