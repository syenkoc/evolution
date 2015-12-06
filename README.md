# evolution

This project implements differential evolution in pure Java 8. Differential
evolution (DE) is a heuristic optimization algorithm. This projects exists
mainly as an outgrowth of work I did fitting various models (for which DE
is an indispensable tool). 

Some notable features of this package:
* Pluggable "differentiation" algorithms:
** Classic random from Storn <i>et al.</i>
** The best strategy.
** The directional strategy.
** The multi-nomial strategy from Buhry <i>et al.<i>

* Pluggable recombination algorithms:
** Binomial.
** Exponential.
** Trial.
 
* Support for arbitrary constraints. Specifically, this package implements a 
variant of the algorithm suggested by Mezura-Montes <i>et al.</i> for 
constrained problems. This implementation also incorporates a diversity
mechanism to allow infeasible candidates to explore "interesting"
areas of the search space; while still ensuring that the ultimate solution
is feasible.

* Easy to use: In order to get started with a basic problem, you need only 
implement two relatively trivial interfaces:
** A function to randomly generate a parameter vector.
** A function to measure the fitness of the a parameter vector.

* No runtime dependencies, other than a Java 8 JVM. (JUnit is used for unit testing.)

* Support for multi-threaded optimization, using the Fork/Join pattern.

* Functional-style support.

