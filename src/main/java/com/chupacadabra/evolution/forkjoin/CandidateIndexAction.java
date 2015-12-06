/*
 * $Id$
 * 
 * Copyright (c) 2015 Fran Lattanzio
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.chupacadabra.evolution.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

import com.chupacadabra.evolution.Candidate;
import com.chupacadabra.evolution.DifferentialEvolutionSettings;
import com.chupacadabra.evolution.DifferentialEvolutionProblem;
import com.chupacadabra.evolution.DifferentialEvolutionState;
import com.chupacadabra.evolution.DiversityPolicy;
import com.chupacadabra.evolution.RandomSource;
import com.chupacadabra.evolution.SelectionPolicy;
import com.chupacadabra.evolution.pool.CandidatePool;

/**
 * The action for a single candidate.
 */
final class CandidateIndexAction
	extends RecursiveAction
{

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The problem.
	 */
	private DifferentialEvolutionProblem problem;

	/**
	 * The settings.
	 */
	private DifferentialEvolutionSettings settings;

	/**
	 * The current state.
	 */
	private DifferentialEvolutionState state;

	/**
	 * The parent index.
	 */
	private int parentIndex;

	/**
	 * Current pool.
	 */
	private CandidatePool currentPool;

	/**
	 * Next pool.
	 */
	private CandidatePool nextPool;
	
	/**
	 * Constructor.
	 * 
	 * @param problem The problem.
	 * @param settings The settings.
	 * @param state State.
	 * @param parentIndex The index to process.
	 * @param currentPool The current pool.
	 * @param nextPool The next pool.
	 */
	CandidateIndexAction(final DifferentialEvolutionProblem problem,
			final DifferentialEvolutionSettings settings,
			final DifferentialEvolutionState state, 
			final int parentIndex,
			final CandidatePool currentPool, 
			final CandidatePool nextPool)
	{
		this.problem = problem;
		this.settings = settings;
		this.state = state;
		this.parentIndex = parentIndex;
		this.currentPool = currentPool;
		this.nextPool = nextPool;
	}

	/**
	 * @see java.util.concurrent.RecursiveAction#compute()
	 */
	@Override
	protected void compute()
	{
		// generate feasible and violating children (infeasible candidates are
		// filtered out).
		List<Candidate> children = getChildren();

		if(children.isEmpty())
		{
			// all children were infeasible - there is no work to do!
			return;
		}

		// now grab the best child.
		SelectionPolicy selectionPolicy = settings.getSelectionPolicy();
		RandomSource randomSource = settings.getRandomSource();
		Candidate bestChild = selectionPolicy.select(state, randomSource, children);

		// see if best child should replace parent in the pool.
		Candidate parent = currentPool.getCandidate(parentIndex);
		int bestIndex = currentPool.getBestCandidateIndex();

		if(parentIndex == bestIndex)
		{
			// in this case, the child can only replace the parent if it is
			// feasible and has better fitness.
			if(bestChild.isFeasible() && (bestChild.getFitness() < parent.getFitness()))
			{
				nextPool.setCandidate(parentIndex, bestChild);
			}
		}
		else
		{
			// check diversity policy to see if we should compare based only on
			// fitenss.
			DiversityPolicy diversityPolicy = settings.getDiversityPolicy();
			double diversity = diversityPolicy.getDiversity(state, randomSource);
			double ud = randomSource.nextDouble();

			if(ud < diversity)
			{
				// in this case, we only check the fitness, regardless of the
				// selection policy.
				if(bestChild.getFitness() < parent.getFitness())
				{
					nextPool.setCandidate(parentIndex, bestChild);
				}
			}
			else
			{
				// otherwise apply the selection policy.
				Candidate bestCandidate = selectionPolicy.select(state, randomSource, parent, bestChild);
				nextPool.setCandidate(parentIndex, bestCandidate);
			}
		}
	}

	/**
	 * Get generated child candidates.
	 * 
	 * @return All children.
	 */
	private List<Candidate> getChildren()
	{
		int childCount = settings.getChildrenPerCandidate();
		List<ForkJoinTask<Candidate>> childTasks = new ArrayList<ForkJoinTask<Candidate>>(
				childCount);

		// fork off requests to generate all children.
		for(int index = 0; index < childCount; index++)
		{
			CreateChildCandidateTask childTask = new CreateChildCandidateTask(
					problem, settings, state, index, currentPool);
			childTasks.add(childTask.fork());
		}

		// collect them all.
		List<Candidate> children = new ArrayList<Candidate>(childCount);
		for(int index = 0; index < childCount; index++)
		{
			// wait for task to finish and grab the candidate.
			Candidate candidate = childTasks.get(index).join();
			if(candidate != null)
			{
				// the child comes back null if it was infeasible.
				children.add(candidate);
			}
		}

		return children;
	}

}
