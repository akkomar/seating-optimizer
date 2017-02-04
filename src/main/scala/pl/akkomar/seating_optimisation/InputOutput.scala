package pl.akkomar.seating_optimisation

case class Input(planeDimensions: PlaneDimensions, passengerGroups: Seq[PassengerGroup])

case class PlaneDimensions(seatsPerRow: Int, numberOfRows: Int)

case class PassengerGroup()

case class Passenger(id: Int, windowPreferred: Boolean = false)

object InputReader {
  def read(filePath: String): Input = {
    val fileContents = scala.io.Source.fromFile(filePath).getLines().toSeq

    val rawPlaneDimensions = fileContents.head.split(" ")
    val seatsPerRow = rawPlaneDimensions(0).toInt
    val rowsNum = rawPlaneDimensions(1).toInt

    Input(PlaneDimensions(seatsPerRow, rowsNum), Seq())
  }
}


case class Output(seatingMap: Array[Array[Int]], passengerSatisfactionPercent: Int)

object OutputWriter {
  def toString(output: Output): String = {
    output.seatingMap.map(_.mkString(" ")).mkString("\n") +
      s"\n${output.passengerSatisfactionPercent}%"
  }
}