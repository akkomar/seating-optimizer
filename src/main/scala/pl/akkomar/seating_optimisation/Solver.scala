package pl.akkomar.seating_optimisation

trait Solver {
  def arrangeSeats(input: Input): Output
}

/**
  * Solves seating arrangement problem using first-fit algorithm
  */
object FirstFitSolver extends Solver {

  override def arrangeSeats(input: Input): Output = {
    //allocate empty seating map
    val dim = input.planeDimensions
    val seatMap = Array.fill(dim.numberOfRows, dim.seatsPerRow)(None: Option[Int])

    val splitGroups = collection.mutable.ListBuffer[PassengerGroup]()

    //allocate passengers
    input.passengerGroups.foreach { group =>
      val allocatedTogether = tryToAllocateGroup(group, seatMap)
      if (!allocatedTogether) {
        //we failed to allocate group because there were no free rows, let's split it and
        // try again with individual passengers in the end
        splitGroups ++= group.passengers.map(p => PassengerGroup(Seq(p)))
      }
    }

    //allocate split groups
    splitGroups.foreach { group => tryToAllocateGroup(group, seatMap) }

    val satisfactionPercent = PassengerSatisfactionCalculator.calculatePassengerSatisfactionPercent(input, seatMap)

    Output(seatMap, satisfactionPercent)
  }

  private def freeSeatsRemaining(seatMap: Array[Array[Option[Int]]]): Boolean = {
    seatMap.flatten.contains(None)
  }

  /**
    * Tries to allocate group next to each other, returns true is succeeded,
    * if failed, no alocation is done and false is returned
    */
  private def tryToAllocateGroup(group: PassengerGroup, seatMap: Array[Array[Option[Int]]]): Boolean = {
    //put those preferring windows in the beginning
    val passengers = group.passengers.sortWith((first, _) => first.windowPreferred)
    val groupSize = passengers.size
    val windowPreferred = passengers.exists(_.windowPreferred)

    //find first available row
    val availableRowOption = seatMap.zipWithIndex.find { case (row, rowNumber) => row.count(_.isEmpty) >= groupSize }.map(_._2)
    //if needed, find first available row with free window seat
    val availableRowWithWindowOption = if (windowPreferred) {
      seatMap.zipWithIndex.find { case (row, rowNumber) => row.count(_.isEmpty) >= groupSize && (row.head.isEmpty || row.last.isEmpty) }.map(_._2)
    } else {
      None
    }

    availableRowWithWindowOption.orElse(availableRowOption) match {
      case None => false
      case Some(rowNumber) =>
        var nextFreeSeat = seatMap(rowNumber).indexOf(None)
        passengers.foreach { p =>
          seatMap(rowNumber)(nextFreeSeat) = Some(p.id)
          nextFreeSeat += 1
        }
        true
    }
  }
}
