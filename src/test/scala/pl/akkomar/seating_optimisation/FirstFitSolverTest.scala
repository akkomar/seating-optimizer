package pl.akkomar.seating_optimisation

import org.scalatest.{FlatSpec, Matchers}

class FirstFitSolverTest extends FlatSpec with Matchers {

  "First-fit decreasing solver" should "proerly solve test example" in {
    val output = FirstFitSolver.arrangeSeats(ExampleTestData.exampleInput)

    output.passengerSatisfactionPercent shouldEqual ExampleTestData.exampleOutput.passengerSatisfactionPercent
    output.seatingMap.deep shouldEqual ExampleTestData.exampleOutput.seatingMap.deep
  }
}
