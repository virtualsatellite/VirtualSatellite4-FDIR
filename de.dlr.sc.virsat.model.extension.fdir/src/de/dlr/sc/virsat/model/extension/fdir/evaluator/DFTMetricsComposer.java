package de.dlr.sc.virsat.model.extension.fdir.evaluator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IMetricVisitor;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.PointAvailability;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.FaultTreeNodePlus;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Module;

public class DFTMetricsComposer implements IMetricVisitor {
	
	private ModelCheckingResult composedResult;
	private FaultTreeNode topLevelNode;
	private List<FaultTreeNodePlus> childrenPlus;
	private List<ModelCheckingResult> subModuleResults;
	
	public ModelCheckingResult compose(List<ModelCheckingResult> subModuleResults, IMetric[] metrics, Module topLevelModule) {
		this.topLevelNode = topLevelModule.getRootNode();
		this.childrenPlus = topLevelModule.getModuleRoot().getChildren();
		this.subModuleResults = subModuleResults;
		this.composedResult = new ModelCheckingResult();
		
		for (IMetric metric : metrics) {
			metric.accept(this);
		}
		
		return composedResult;
	}
	
	private long getK(FaultTreeNode node, Collection<?> children) {
		long k = children.size();
		if (node instanceof Fault) {
			k = 1;
		} else if (node instanceof VOTE) {
			k = ((VOTE) node).getVotingThreshold();
		}
		
		return k;
	}
	
	private double composeProbabilities(List<Double> probabilities, long k) {
		double composedProbability = 1;
		if (k == 1) {
			for (double failRate : probabilities) {
				composedProbability *= 1 - failRate;
			}
			composedProbability = 1 - composedProbability;
		} else {
			for (double failRate : probabilities) {
				composedProbability *= failRate;
			}
		} 
		
		// TODO: Else for general vote gates
		
		return composedProbability;
	}
	
	@Override
	public void visit(Reliability reliabilityMetric) {
		long k = getK(topLevelNode, childrenPlus);
		
		int countFailRates = 0;
		for (ModelCheckingResult subModuleResult : subModuleResults) {
			countFailRates = Math.max(countFailRates, subModuleResult.getFailRates().size());
		}
		
		for (int i = 0; i < countFailRates; ++i) {
			List<Double> childFailRates = new ArrayList<>(countFailRates);
			
			for (ModelCheckingResult subModuleResult : subModuleResults) {
				if (i < subModuleResult.getFailRates().size()) {
					childFailRates.add(subModuleResult.getFailRates().get(i));
				} else {
					childFailRates.add(1d);
				}
			}
			
			double composedFailRate = composeProbabilities(childFailRates, k);
			composedResult.getFailRates().add(composedFailRate);
			
			if (composedFailRate == 1) {
				break;
			}
		}
	}

	@Override
	public void visit(MTTF mttfMetric) {
		int countFailRates = composedResult.getFailRates().size();
		double[] x = new double[countFailRates];
		for (int i = 0; i < countFailRates; ++i) {
			x[i] = i;
		}
		
		double[] y = new double[countFailRates];
		for (int i = 0; i < countFailRates; ++i) {
			y[i] = 1 - composedResult.getFailRates().get(i);
		}
		
		UnivariateInterpolator interpolator = new SplineInterpolator();
		UnivariateFunction function = interpolator.interpolate(x, y);
		
		UnivariateIntegrator integrator = new SimpsonIntegrator();
		double integral = integrator.integrate(SimpsonIntegrator.DEFAULT_MAX_ITERATIONS_COUNT, function, 0, countFailRates - 1);
		composedResult.setMeanTimeToFailure(integral);
	}

	@Override
	public void visit(PointAvailability pointAvailabilityMetric) {
		long k = getK(topLevelNode, childrenPlus);
		
		int countPoints = 0;
		for (ModelCheckingResult subModuleResult : subModuleResults) {
			countPoints = Math.max(countPoints, subModuleResult.getPointAvailability().size());
		}
		
		for (int i = 0; i < countPoints; ++i) {
			List<Double> childPointAvailabilities = new ArrayList<>(countPoints);
			
			for (ModelCheckingResult subModuleResult : subModuleResults) {
				if (i < subModuleResult.getFailRates().size()) {
					childPointAvailabilities.add(subModuleResult.getPointAvailability().get(i));
				} else {
					childPointAvailabilities.add(1d);
				}
			}
			
			double composedAvailability = composeProbabilities(childPointAvailabilities, k);
			composedResult.getPointAvailability().add(composedAvailability);
		}		
	}

	@Override
	public void visit(SteadyStateAvailability steadyStateAvailabilityMetric) {
		long k = getK(topLevelNode, childrenPlus);
	
		List<Double> childSteadyStateAvailabilities = new ArrayList<>();
		
		for (ModelCheckingResult subModuleResult : subModuleResults) {
			childSteadyStateAvailabilities.add(subModuleResult.getSteadyStateAvailability());
		}
		
		double composedSteadyStateAvailability = composeProbabilities(childSteadyStateAvailabilities, k);
		composedResult.setSteadyStateAvailability(composedSteadyStateAvailability);
	}
}
