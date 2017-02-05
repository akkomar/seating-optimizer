package pl.akkomar.seating_optimisation

object ArrangeSeats {
  def main(args: Array[String]): Unit = {
    require(args.length == 1, "Please provide one argument - path to input file")
    val inputFilePath = args(0)

    val input = InputReader.read(inputFilePath)

    val output = FirstFitSolver.arrangeSeats(input)

    println("result:")
    println(OutputWriter.toString(output))
  }
}
