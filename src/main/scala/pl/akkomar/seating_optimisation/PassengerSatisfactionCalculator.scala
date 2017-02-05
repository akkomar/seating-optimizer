package pl.akkomar.seating_optimisation

object PassengerSatisfactionCalculator {
  def calculatePassengerSatisfactionPercent(input: Input, seatMap: Array[Array[Option[Int]]]): Int = {
    val numberOfPassengers = input.passengerGroups.map(_.passengers.size).sum
    val numberOfHappyPassengers = input.passengerGroups.map { group =>
      val passengers = group.passengers
      val allocatedSeats = passengers.flatMap(getSeatOf(_, seatMap))

      val everyoneHasSeat: Boolean = allocatedSeats.size == passengers.size
      lazy val everyoneInSameRow: Boolean = allocatedSeats.map(_._1).forall(_ == allocatedSeats.head._1)
      lazy val unhappyWithoutWindow: Int = passengers.filter(_.windowPreferred).map { p =>
        val seatNumberOption = getSeatOf(p, seatMap).map(_._2)
        val seatIsNearWindow = seatNumberOption.exists { seatNumber =>
          seatNumber == 0 || seatNumber == (input.planeDimensions.seatsPerRow - 1)
        }
        seatIsNearWindow
      }.count(_ == false)

      if (everyoneHasSeat && everyoneInSameRow) passengers.size - unhappyWithoutWindow else 0
    }.sum

    (numberOfHappyPassengers / numberOfPassengers.toDouble * 100).toInt
  }

  /**
    * Returns Some(row,seat) of a passenger, or None if not allocated
    */
  private def getSeatOf(p: Passenger, seatMap: Array[Array[Option[Int]]]): Option[(Int, Int)] = {
    seatMap.zipWithIndex.map { case (row, rowNumber) =>
      val seat = row.indexOf(Some(p.id))
      if (seat != -1) Some(rowNumber, seat) else None
    }.find(_.isDefined).flatten
  }
}
