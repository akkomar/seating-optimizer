package pl.akkomar.seating_optimisation

case class Input(planeDimensions: PlaneDimensions, passengerGroups: Seq[PassengerGroup])

case class PlaneDimensions(seatsPerRow: Int, numberOfRows: Int)

case class PassengerGroup(passengers: Seq[Passenger])

case class Passenger(id: Int, windowPreferred: Boolean = false)

object InputReader {
  def read(filePath: String): Input = {
    val fileContents = scala.io.Source.fromFile(filePath).getLines().toSeq

    val rawPlaneDimensions = fileContents.head.split(" ")
    val seatsPerRow = rawPlaneDimensions(0).toInt
    val rowsNum = rawPlaneDimensions(1).toInt

    val passengerGroups: Seq[PassengerGroup] = fileContents.tail.map { line =>
      val passengers = line.split(" ").map(parsePassenger)
      PassengerGroup(passengers)
    }

    Input(PlaneDimensions(seatsPerRow, rowsNum), passengerGroups)
  }

  private def parsePassenger(p: String): Passenger = {
    val windowPreferred = p.contains("W")
    val id = p.replace("W", "").toInt
    Passenger(id, windowPreferred)
  }
}


case class Output(seatingMap: Array[Array[Option[Int]]], passengerSatisfactionPercent: Int)

object OutputWriter {
  def toString(output: Output): String = {
    output.seatingMap.map(row => row.flatten.mkString(" ")).mkString("\n") +
      s"\n${output.passengerSatisfactionPercent}%"
  }
}