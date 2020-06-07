package de.dlr.sc.virsat.fdir.core.matrix.iterator;

import java.util.Arrays;
import java.util.List;

import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.matrix.IMatrix;

/**
 * Performs a long run average computation of the Steady State unAvailability. 
 * Only applicable for strongly connected end components.
 * The algortihm is based on the paper:
 * 
 * Guck, Dennis, et al. "Modelling, reduction and analysis of Markov automata (extended version)." arXiv preprint arXiv:1305.7050 (2013).
 * 
 * @author muel_s8
 *
 */

public class SSAIterator<S extends MarkovState> extends MatrixIterator implements IDelegateIterator<S> {

	private double[] oldValues;
	private IMatrixIterator failCostIterator;
	private IMatrixIterator totalCostIterator;

	public SSAIterator(IMatrix matrix, double[] baseCostFails, double[] baseCostTotal) {
		super(matrix, new double[baseCostFails.length]);
		
		failCostIterator = new BellmanIterator(matrix, baseCostFails);
		totalCostIterator = new BellmanIterator(matrix, baseCostTotal);
		oldValues = values.clone();
	}

	@Override
	public void iterate() {
		for (int i = 0; i < values.length; ++i) {
			oldValues[i] = values[i];
		}
		
		failCostIterator.iterate();
		totalCostIterator.iterate();
		
		for (int i = 0; i < values.length; ++i) {
			values[i] = failCostIterator.getValues()[i] / totalCostIterator.getValues()[i];
		}
	}
	
	@Override
	public void delegateProbabilisticUpdate(int stateIndex, double value, List<MarkovTransition<S>> transitions) {
		failCostIterator.getValues()[stateIndex] = MarkovTransition.getExpectationValue(transitions, failCostIterator.getOldValues());
		totalCostIterator.getValues()[stateIndex] = MarkovTransition.getExpectationValue(transitions, totalCostIterator.getOldValues());
	}
	
	@Override
	public double[] getOldValues() {
		return oldValues;
	}
}
