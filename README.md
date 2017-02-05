# Seating arrangement optimizer
Optimizer uses first-fit algorithm to arrange seats. It won't always give optimal solution, but is fast and not 
overly complicated. One easy and good optimization would be using _first-fit decreasing algorithm_ (by sorting groups),
but that would give slightly different result for the example input.

## Running
### Tests
```bash
sbt test
```
### Application
```bash
sbt "run-main pl.akkomar.seating_optimisation.ArrangeSeats src/test/resources/input.txt"
```
or substitute `src/test/resources/input.txt` with custom input file.