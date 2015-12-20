# Differential Evolution

This project implements differential evolution in pure Java 8. Differential
evolution (DE) is a metaheuristic optimization algorithm. Optimization is done
iteratively, successively improving candidate solution with respect to a fitness
measure. DE makes very few assumptions about the structure of the problem, and, unlike
many other methods, does not require the gradient. Thus, differential evolution can tackle 
problems that not differentiable, are noisy, or have multiple minima. This implementation
can also handle arbitrary (non-linear) constraints. The trade-off is that DE is not 
guaranteed to find an optimal solution, either local or global.

As mentioned, at a high-level, the DE algorithm consists of continually improving a pool 
of candidate parameter vectors. The pool of candidates are first initialized
by randomly generating candidates within the boundaries of the problem. At each
step of the algorithm, each candidate builds a number of <i>child</i> vectors
by means of two fundamental operations, <i>differentiation</i> and <i>recombination</i>.
Finally, <i>diversity</i> and <i>selection</i> are carried out first determine which 
is the best child; and, second, if the said child should replace the parent 
in the candidate pool.

## Problems 

A differential evolution <i>problem</i> consists (minimally) of the following:
* The <i>dimension</i> of the problem. This is length (in <i>R</i><sup>n</sup>)
of the candidate vectors.
* A <i>random parameter vector generator</i> that randomly constructs candidates 
over the search space.  
* A <i>fitness function</i> that measures how well a candidate vector is to
achieving the optimal value. This behaves like a cost function in the sense that
lower is better. 

In order to handle constraints, we use a modified version of the method proposed
by Mezura-Montes <i>et al</i>. In this scheme, candidates are first classified 
with respect to some user-defined set of constraints. A <i>feasible</i> candidate 
is one that is not violating any constraints. A candidate <i>in violation</i> is 
violating one or more of the constraints, but it is still possible to compute a 
valid fitness for such a candidate. Finally, a candidate is <i>infeasible</i>
if it not possible to compute a fitness measure. This ternary candidate classification 
exists because we <i>may</i> want to keep a violating candidate in the pool 
if it is exploring an "interesting" area of the search space; whereas we want 
to reject outright candidates for which it is impossible to compute a fitness 
measure.

For a candidate in violation, a second cost function measure the degree of
violation. This function is <i>not</i> the same as the fitness function, and 
in fact the values from the two function are never directly compared. This 
avoids the complex "tuning" often required with penalty and barrier methods.
Instead, when we are performing selection, we compare candidates according to
the following rules:
* If both candidates are in violation, select the candidate with the lower
violation value.
* If one candidates is in violation but the other is not, select the
non-violating candidate.
* If neither candidate is in violation, select the candidate with the lower 
fitness.

Obviously, if we applied these rules to a pool consisting only of feasible
candidates, no violating candidates would ever enter pool. A <i>diversity
policy</i> (see below) control the degree to which a violating child candidate
will replace its parent, provided of course that the child has a better fitness.
(In short, the diversity policy "short-circuits" the aforementioned selection
criteria and compares candidates based only on their fitness.)
   
Finally, a problem can optionally supply termination criteria. These are user-defined 
functions that indicate whether the optimization should terminate. A few very common 
"canned" criteria are already implemented:
* The optimization reaches a time limit.
* The best candidates achieves a desired fitness value.

These constructs are represented in the problem API as:
* A feasibility function. The default behavior is to classify all candidates are
feasible, so this only needs to be supplied in the case of a constrained 
problem.
* A violation function. The default implementation returns 0 for all candidates.
This function thus only need to be supplied if the feasibility classification
function marks any candidates as in violation.
* A list of termination criteria. The default implementation returns an empty
list.

### Example Problem

Here we present a simple example of how to construct and optimize a problem. The problem in this case
is to optimize the function <code>f(x) = x<sup>2</sup>/10 + 4 * sin(x)</code> on the interval (-10, 10).
If you look at a graph of this function, you will see that it has several local minima, so a gradient-type
optimizer could easily become stuck in depending on starting point. This function has a global minimum
at <code>x ~ -1.49593</code>, where <code>f(x) ~ 

```java
// construct our fitness function.
FitnessFunction fitnessFunction = (double[] x) -> (x[0] * x[0] / 10) + (4 * Math.sin(x[0]));

// on of the most common cases for parameters is that they are described by a n-orthotope, i.e.
// an n-dimensional hypercube. This utility class allows us to easily create such parameter
// functions.
NOrthotopeRandomParametersFunction parameterFunction = new NOrthotopeRandomParametersFunction(1);

// this will distribute the initial values of x on (-10, 10)
parameterFunction.setParameterRange(0, -10, 10);

// now, set up a problem. The main problem API is an interface of getters; this implementation 
// allows us to set everything directly.
SimpleDifferentialEvolutionProblem problem = new SimpleDifferentialEvolutionProblem();
problem.setDimension(1);
problem.setRandomParametersFunction(parameterFunction);
problem.setFitnessFunction(fitnessFunction);
		
// create a new optimizer. This will use the common fork-join pool.
ForkJoinDifferentialEvolutionOptimizer optimizer = new ForkJoinDifferentialEvolutionOptimizer();
		
// now just get the result!
DifferentialEvolutionResult result = optimizer.optimize(problem);
		
// extract x and f(x).
double x = result.getBestCandidate().getParameters()[0];
double fx = result.getBestCandidate().getFitness();
```

## Policies 

There are myriad variants of DE, but fundamentally they consist only of 
changing the differentiation, recombination, diversity, and selection methods. 
This package uses the 
<a href="https://en.wikipedia.org/wiki/Strategy_pattern">policy pattern</a> to
allow the user to control the inner workings of the optimizer. Specifically, the
<code>(DifferentiationPolicy)</code>,
<code>RecombinationPolicy</code>, 
<code>DiversityPolicy</code>, and 
<code>SelectionPolicy</code> functional interfaces 
allow you to plug in different strategies at run time.

These policies are grouped into a larger 
<code>DifferentialEvolutionSettings</code> 
class. Settings are optional,
in the sense that "reasonable" defaults will be supplied if you do not configure
the settings yourself. In addition to the aforementioned policy functions, the 
settings also allow you to control several other optimizer properties: 
 
1. <i>Maximum Generation</i>: This is simply the maximum number of generations that
the optimizer will perform - assuming that no user-defined termination criterion (see 
below) is met. Note that unlike (most) other optimization algorithms, it is 
not considered an "error" to terminate due to reaching the maximum generation.
This defaults to 333. 
2. <i>Pool Size</i>: The number of candidates in the pool. The default is 71.
3. <i>Number of Children</i>: The number of child that each candidate will generate
per generation. Thus, the total number of children per generation is the pool size
times the number of children. The default is 1. 
4. <i>Replacement Policy</i>: This determines when a fitter child candidate will 
replace its parent. The two options are to replace immediately, <i>i.e.</i> it will 
visible to the algorithm intra-generation; and afterwards in bulk. The default is
to replace immediately.
5. <i>Exception Handling</i>: Controls how to deal with a runtime exception tossed by 
user-level code. The two options are to propagate the exception; or to terminate the
algorithm and return the current optimum. The default is to propagate.
6. <i>Random Generator</i>: The interface <code>(RandomSource)</code> represents
an abstraction of the 
<code>(java.util.Random)[https://docs.oracle.com/javase/8/docs/api/java/util/Random.html]
to allow you plug in different psuedo-random number generators. The default implementation
is a one backed by bog-standard <code>java.util.Random</code>.

 

## Installation
This package is pushed to Maven Central, with the following coordinates:


## Dependencies
There are no runtime dependencies. JUnit is brought in via Maven for unit testing only.


## Contributing
1. Fork it.
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request.


## History
1.0.0, 1/1/2016: Initial release.


## License
The MIT License (MIT)

Copyright (c) 2015-2016 Fran Lattanzio

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
