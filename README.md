# Differential Evolution

This project implements differential evolution in pure Java 8. Differential
evolution (DE) is a metaheuristic optimization algorithm. Optimization is done
iteratively, successively improving candidate solutions with respect to a fitness
measure. DE makes very few assumptions about the structure of the problem, and, unlike
many other methods, does not require the gradient. Thus, differential evolution can tackle 
problems that not differentiable, are noisy, or have multiple minima. This implementation
can also handle arbitrary (non-linear) constraints. The trade-off is that DE is not 
guaranteed to find an optimal solution, either local or global.

As mentioned, at a high-level, the DE algorithm consists of continually improving a pool 
of candidate parameter vectors. The pool of candidates are first initialized
by randomly generating candidates within the boundaries of the problem. At each
step of the algorithm, every candidate builds a number of <i>child</i> vectors
by means of two fundamental operations: <i>differentiation</i> and <i>recombination</i>.
Finally, <i>diversity</i> and <i>selection</i> are carried out first determine which 
is the best child; and, second, if the said child should replace the parent 
in the candidate pool.

## Problems 

A <i><a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/DifferentialEvolutionProblem.html">differential evolution problem</a></i> 
consists (minimally) of the following:
* The <i>dimension</i> of the problem. This is length (in <i>R</i><sup>n</sup>)
of the candidate vectors.
* A <i><a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/RandomParametersFunction.html">random parameter vector generator</a></i> 
that randomly constructs candidates over the search space.  
* A <i><a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/FitnessFunction.html">fitness function </a></i> 
that measures how well a candidate vector is to achieving the optimal value. This behaves 
like a cost function in the sense that lower is better. 

In order to handle constraints, we use a modified version of the method proposed
by Mezura-Montes <i>et al</i>. In this scheme, candidates are first 
<a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/FeasibilityFunction.html">classified</a> 
with respect to some user-defined set of constraints. A <i>feasible</i> candidate 
is one that is not violating any constraints. A candidate <i>in violation</i> is 
violating one or more of the constraints, but it is still possible to compute a 
valid fitness for such a candidate. Finally, a candidate is <i>infeasible</i>
if it not possible to compute a fitness measure. This ternary candidate classification 
exists because we may want to keep a violating candidate in the pool 
if it is exploring an "interesting" area of the search space; whereas we want 
to reject outright candidates for which it is impossible to compute a fitness 
measure.

For a candidate in violation, a 
<i><a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/ViolationFunction.html">violation function</a></i>
(essentially a <i>second</i> cost function) measures the degree of violation. 
This function is <b>not</b> the same as the fitness function, and, 
in fact, the values from the two function are never directly compared. This 
avoids the complex cost-function "tuning" (often) required with penalty and 
barrier methods.

By default, problems assume that all parameter vectors are feasible; and the 
violation of all candidates is 0. 
   
Finally, a problem can optionally supply 
<i><a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/TerminationCriterion.html">termination criteria</a></i>.
 These are user-defined 
functions that indicate whether the optimization should terminate. A few very common 
"canned" criteria are already implemented:
* The optimization reaches a 
<a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/MaximumTime.html">time limit</a>. 
* The best candidate achieves
<a href="http://syenkoc.github.io/evolution/javadocs/com/chupacadabra/evolution/FitnessAchieved.html">a desired level of fitness</a>.
By default, 

### Simple Example Problem

Here we present a simple example of how to construct and optimize a problem. The problem in this case
is to optimize the function <code>f(x) = x<sup>2</sup>/10 + 4 * sin(x)</code> on the interval (-10, 10).
If you look at the graph of this function, you will see that it has several local minima, so a gradient-type
optimizer could easily become stuck in depending on starting point. This function has a global minimum
at <code>x ~ -1.49593</code>, where <code>f(x) ~ -3.76501</code>.

<img src="http://syenkoc.github.io/evolution/images/example.png" alt="Example function" style="width:302px; height=184px">

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
		
// create a suitable optimizer.
DifferentialEvolutionOptimizer optimizer = new SerialDifferentialEvolutionOptimizer();
		
// now just get the result!
DifferentialEvolutionResult result = optimizer.optimize(problem);
		
// extract x and f(x).
double x = result.getBestCandidate().getParameters()[0];
double fx = result.getBestCandidate().getFitness();
```

## Policies 

There are myriad variants of DE, but fundamentally they consist only of 
changing the differentiation, recombination, diversity, and selection methods. 
This framework uses the 
<a href="https://en.wikipedia.org/wiki/Strategy_pattern">policy pattern</a> to
allow the user to control the inner workings of the optimizer. Specifically, the
<i><a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/DifferentiationPolicy.html">differentiation policy</a></i>,
<i><a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/RecombinationPolicy.html">recombination policy</a></i>,
<i><a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/DiversityPolicy.html">diversity policy</a></i>, and 
<i><a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/SelectionPolicy.html"selection policy</a></i>
functional interfaces allow you to plug in different strategies at run time.

These policies are grouped into a larger 
<i><a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/DifferentiationEvolutionSettings.html">settings</a></i>,
class. Settings are optional,
in the sense that "reasonable" defaults will be supplied if you do not configure
settings yourself. In addition to the aforementioned policy functions, the 
settings also allow you to control several other optimizer properties: 
 
1. The <i>Maximum Generation</i> is simply the maximum number of generations that
the optimizer will perform - assuming that no user-defined termination criterion (see 
below) is met. Note that unlike (most) other optimization algorithms, it is 
not considered an "error" to terminate due to reaching the maximum generation.
This defaults to 1729 (chosen solely because it is a taxicab number <i>and</i> a 
Carmichael number). 
2. The <i>Pool Size</i> is the number of candidates in the pool. The default is 128. 
A good rule-of-thumb is to set this to roughly 10x the dimension of the problem. 
3. The <i>Number of Children</i> that each candidate will generate
per generation. Thus, the total number of children per generation is the pool size
times the number of children. The default is 4. 
4. The <i>Replacement Policy</i> determines when a more fit child candidate will 
replace its parent. The two options are to replace immediately, <i>i.e.</i> it will 
visible to the algorithm intra-generation; and afterwards. The default is
to replace immediately.
5. <i>Exception Handling</i> controls how to deal with a runtime exception tossed by 
user-level code. The two options are to propagate the exception; or to terminate the
algorithm and return the current optimum. The default is to propagate.
6. A <i>Random Generator</i> represents an abstraction of the 
<code><a href="https://docs.oracle.com/javase/8/docs/api/java/util/Random.html">java.util.Random</a></code>
to allow you plug in different (pseudo) random number generators. The default implementation
is a one backed by a bog-standard <code>java.util.Random</code> instance.

### Differentiation
Differentiation generates trial vectors  

### Recombination
Recombination breeds the trial vectors with the parent to produce <i>child</i> vectors. 

### Selection
Selection determines how the optimizer chooses between two candidates. The only provided, and thus default,
implementation uses Deb's selection criteria. When comparing two candidates, these rules are:
* If both candidates are in violation, select the candidate with the lower
violation value.
* If one candidates is in violation but the other is not, select the
non-violating candidate.
* If neither candidate is in violation, select the candidate with the lower 
fitness.

### Diversity
The diversity policy determines the degree to which the optimizer bypasses the selection criteria and allows
candidates into the pool based <i>solely</i> only the fitness measure. Two diversity policies are included
by default:
<ul>
<li>The 
<i><a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/NoDiversityPolicy.html">No diversity</a></i>
policy does not allow any violating candidate into the pool.</li>
<li>The 
<i><a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/FixedDiversityPolicy.html">fixed diversity</a></i> 
policy considers candidates based only on fitness with a fixed probability. <code>.1</code> is a reasonable
choice for the probability in many cases.</li>
</ul>


## Optimizers
Finally, the 
<i><a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/DifferentialEvolutionOptimizer.html">optimizer interface</a></i>
takes a problem (and, optionally, settings) and produces a result. The result contains the optimal candidate and
the reason for termination (along with some timing data). There are currently two implementations:
<ul>
 <li>The
  <a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/SerialDifferentialEvolutionOptimizer">serial optimizer</a>.
  This implementation performs optimization directly in the invoking thread.
 </li>
 <li>The
  <a href="http://syenkoc.github.io/evolution/javadocs/index.html?com/chupacadabra/evolution/SerialDifferentialEvolutionOptimizer">serial optimizer</a>.
  This implementation uses the 
  <a href="https://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html">fork-join model</a>
  to potentially distribute the work across several threads. 
 </li>
</ul>


### Parallelism and Threadsafety
Differential evolution is amenable to parallel evaluation. The aforementioned fork-join pool based 
implementation process each generation in parallel. (The serial optimizer obviously processes each
generation serially.) The fork-join optimizer has it's own
<a href="http://syenkoc.github.io/evolution/javadocs/com/chupacadabra/evolution/ForkJoinDifferentialEvolutionOptimizerConfiguration">configuration</a>
that determines the levels of parallelism (essentially controlling the size at which the optimizer
decides to "execute directly" vs. forking-and-aggregating). 

If you use the parallel optimizer, all of the problem and policy functions must be safe for use by multiple
threads. All of the policy implementations provided with the framework are already threadsafe. You
can obtain a threadsafe wrapper around any problem or policy function using the 
<a href="http://syenkoc.github.io/evolution/javadocs/com/chupacadabra/evolution/threadsafe/Threadsafe.html">Threadsafe</a>
provider. This provider allows you to create threadsafe decorators using synchronization 
or 
<a href="https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/locks/ReentrantLock.html">java locks</a>.

Note that the parallel optimizer is <i>not</i> always faster! The parallel optimizer incurs some additional
locking overhead, so depending on the cost of the fitness function, size of the pool, number of children, 
<i>etc.</i> the serial optimizer may out perform it.


## Installation
This package is pushed to Maven Central, with the following coordinates:


## Dependencies
There are no runtime dependencies. JUnit is brought in via Maven for unit testing only.


## Javadocs
The Javadocs can be found here: <a href="http://syenkoc.github.io/evolution/javadocs/index.html">http://syenkoc.github.io/evolution/javadocs/index.html</a>


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
