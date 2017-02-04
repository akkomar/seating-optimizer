package pl.akkomar.seating_optimisation

import scala.reflect.ClassTag

trait Solver {
  def arrangeSeats(input: Input): Output
}

/**
  * Solves seating arrangement problem using first-fit decreasing algorithm
  */
object FirstFitDecreasingSolver extends Solver{
  override def arrangeSeats(input: Input): Output = {
    val dim = input.planeDimensions
    Array.fill(dim.numberOfRows, dim.seatsPerRow)(None:Option[Int])
    //sort passenger groups

    ???
  }
}