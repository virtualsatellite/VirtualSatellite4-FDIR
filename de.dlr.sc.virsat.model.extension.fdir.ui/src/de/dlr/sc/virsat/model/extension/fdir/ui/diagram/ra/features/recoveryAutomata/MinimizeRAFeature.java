/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.recoveryAutomata;

import org.eclipse.elk.core.service.DiagramLayoutEngine;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.ui.IWorkbenchPart;

import de.dlr.sc.virsat.graphiti.util.DiagramHelper;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.ARecoveryAutomatonMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.ComposedMinimizer;

/**
 * Minimizes the RA
 * @author yoge_re
 *
 */
public class MinimizeRAFeature extends AbstractCustomFeature {
	
	/**
	 * 
	 * @param fp featureProvider
	 */
	public MinimizeRAFeature(IFeatureProvider fp) {
		super(fp);
	}

	
	@Override
	public String getDescription() {
		return "Minimizes the Recovery Automaton";
	}


	@Override
	public String getName() {
		return "Minimize RA";
	}


	@Override
	public void execute(ICustomContext context) {
		PictogramElement pe = context.getPictogramElements()[0];
		
		Object o = getBusinessObjectForPictogramElement(pe);
		if (o instanceof State) {
			State state = (State) o;
			RecoveryAutomaton ra = state.getParentCaBeanOfClass(RecoveryAutomaton.class);
			ARecoveryAutomatonMinimizer minimizer = ComposedMinimizer.createDefaultMinimizer();
			minimizer.minimize(ra);
			updatePictogramElement(pe);
			IWorkbenchPart part = ((DiagramBehavior) getDiagramBehavior()).getDiagramContainer().getWorkbenchPart();
	        DiagramLayoutEngine.invokeLayout(part, null, null);
		}
		

	}
	
	@Override
	public boolean canExecute(ICustomContext context) {
		for (PictogramElement pe : context.getPictogramElements()) {
			Object bo = getBusinessObjectForPictogramElement(pe);
			if (!DiagramHelper.hasBothWritePermission(bo, pe)) {
				return false;
			}
		}
		return true;
	}
}
