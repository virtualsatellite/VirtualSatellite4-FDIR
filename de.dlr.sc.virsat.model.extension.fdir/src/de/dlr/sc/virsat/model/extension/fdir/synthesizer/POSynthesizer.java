/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.synthesizer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.markov.scheduler.IMarkovScheduler;
import de.dlr.sc.virsat.fdir.core.markov.scheduler.MarkovScheduler;
import de.dlr.sc.virsat.fdir.core.markov.scheduler.ScheduleQuery;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFT2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PODFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PONDDFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.ma2beliefMa.BeliefState;
import de.dlr.sc.virsat.model.extension.fdir.converter.ma2beliefMa.MA2BeliefMAConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.ma2beliefMa.OptimalTransitionsSelector;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;

/**
 * A synthesizer that considers partial observability.
 * @author muel_s8
 *
 */

public class POSynthesizer extends ASynthesizer {

	private static final int TICKS = 3;
	
	protected OptimalTransitionsSelector optimalTransitionsSelector = new OptimalTransitionsSelector<BeliefState>();
	
	protected MA2BeliefMAConverter ma2BeliefMAConverter = new MA2BeliefMAConverter(optimalTransitionsSelector);
	protected IMarkovScheduler<BeliefState> scheduler = new MarkovScheduler<>();
	
	@Override
	protected RecoveryAutomaton convertToRecoveryAutomaton(MarkovAutomaton<DFTState> ma, DFTState initialMa, SubMonitor subMonitor) {
		subMonitor = SubMonitor.convert(subMonitor, TICKS);
		
		// Build the actual belief ma
		Path path = Paths.get("C:\\Users\\khan_ax\\Desktop\\Automata\\ma.txt");
		try {
			Files.createFile(path);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try (OutputStream outFile = Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE); 
			PrintStream writer = new PrintStream(outFile)) {
			writer.println(ma.toDot());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(ma.toDot());
		System.out.println(ma.getStates().size());
		MarkovAutomaton<BeliefState> beliefMa = ma2BeliefMAConverter.convert(ma, (PODFTState) initialMa, subMonitor.split(1));
		//System.out.println(beliefMa.toDot());
		path = Paths.get("C:\\Users\\khan_ax\\Desktop\\Automata\\beliefMa.txt");
		try {
			Files.createFile(path);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try (OutputStream outFile = Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE); 
			PrintStream writer = new PrintStream(outFile)) {
			writer.println(beliefMa.toDot());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BeliefState initialBeliefState = ma2BeliefMAConverter.getMaBuilder().getInitialState();
		
		// Create the optimal schedule on the belief ma
		ScheduleQuery<BeliefState> scheduleQuery = new ScheduleQuery<>(beliefMa, initialBeliefState);
		Map<BeliefState, List<MarkovTransition<BeliefState>>> schedule = scheduler.computeOptimalScheduler(scheduleQuery, subMonitor.split(1));
		System.out.println(beliefMa.getStates().size());
		return new Schedule2RAConverter<>(beliefMa, concept).convert(schedule, initialBeliefState, subMonitor.split(1));
	}
	
	@Override
	protected DFT2MAConverter createDFT2MAConverter() {
		DFT2MAConverter dft2MaConverter = new DFT2MAConverter();
		dft2MaConverter.getStateSpaceGenerator().setSemantics(PONDDFTSemantics.createPONDDFTSemantics());
		return dft2MaConverter;
	}
}
