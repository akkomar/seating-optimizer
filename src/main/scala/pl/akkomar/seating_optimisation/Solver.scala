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

    //allocate passengers
    input.passengerGroups.foreach { group =>
      if (freeSeatsRemaining(seatMap)) {
        val groupSize = group.passengers.size
        val allocatedTogether = tryToAllocateGroup(group, seatMap)
        if (!allocatedTogether) {
          //we could leave this for the end in order to get better score in some cases
          group.passengers.foreach(allocateSinglePassenger(_, seatMap))
        }
      }
    }

    val satisfactionPercent = calculatePassengerSatisfactionPercent(input, seatMap)

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
    if (groupSize == 1) {
      allocateSinglePassenger(passengers.head, seatMap)
    } else {
      val windowPreferred = passengers.exists(_.windowPreferred)

      val availableRowOption = if (windowPreferred) {
        //if we don't find empty row with window, we'll fall back to single passenger allocation since they won't be happy anyway
        seatMap.zipWithIndex.find { case (row, rowNumber) => row.head.isEmpty && row.count(_.isEmpty) >= groupSize }.map(_._2)
      } else {
        seatMap.zipWithIndex.find { case (row, rowNumber) => row.count(_.isEmpty) >= groupSize }.map(_._2)
      }

      availableRowOption match {
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

  private def allocateSinglePassenger(passenger: Passenger, seatMap: Array[Array[Option[Int]]]): Boolean = {
    val rowNumberOption = if (passenger.windowPreferred) {
      val r = seatMap.zipWithIndex.find { case (row, rowNumber) => row.head.isEmpty || row.last.isEmpty }.map(_._2)
      if (r.isDefined) {
        r
      } else {
        seatMap.zipWithIndex.find { case (row, rowNumber) => row.contains(None) }.map(_._2)
      }
    } else {
      seatMap.zipWithIndex.find { case (row, rowNumber) => row.contains(None) }.map(_._2)
    }
    rowNumberOption match {
      case None => false
      case Some(rowNumber) =>
        val freeSeat = seatMap(rowNumber).indexOf(None)
        seatMap(rowNumber)(freeSeat) = Some(passenger.id)
        true
    }
  }

  private def calculatePassengerSatisfactionPercent(input: Input, seatMap: Array[Array[Option[Int]]]): Int = {
    val numberOfPassengers = input.passengerGroups.map(_.passengers.size).sum
    val numberOfHappyPassengers = input.passengerGroups.map { group =>
      val passengers = group.passengers
      val allocatedSeats = passengers.flatMap(getSeatOf(_, seatMap))

      val everyoneHasSeat = allocatedSeats.size == passengers.size
      lazy val everyoneInSameRow = allocatedSeats.map(_._1).forall(_ == allocatedSeats.head._1)
      lazy val happyNearWindows = passengers.filter(_.windowPreferred).map { p =>
        getSeatOf(p, seatMap).map(_._2).exists { seatNumber =>
          seatNumber == 0 || seatNumber == (input.planeDimensions.seatsPerRow - 1)
        }
      }.forall(_ == true)

      val rowIsHappy = everyoneHasSeat && everyoneInSameRow && happyNearWindows

      if (rowIsHappy) passengers.size else 0
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
    }.find(_.isDefined).get
  }
}