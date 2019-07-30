/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.validator;

import org.eclipse.core.resources.IMarker;

import de.dlr.sc.virsat.build.validator.external.IStructuralElementInstanceValidator;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.general.IUuid;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.extension.fdir.marker.VirSatFDIRMarkerHelper;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultToleranceRequirement;
import de.dlr.sc.virsat.model.extension.fdir.model.MCSAnalysis;
import de.dlr.sc.virsat.model.extension.fdir.model.ReliabilityAnalysis;
import de.dlr.sc.virsat.model.extension.fdir.model.ReliabilityRequirement;


// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * VirSat DLR FDIR Concept
 * 
 */
public class StructuralElementInstanceValidator extends AStructuralElementInstanceValidator implements IStructuralElementInstanceValidator {

	private VirSatFDIRMarkerHelper vfmHelper = new VirSatFDIRMarkerHelper();

	public static final String WARNING_MIN_RELIABILITY = "Under minimum reliability ";
	public static final String WARNING_MIN_FAULT_TOLERANCE = "Under minimum fault tolerance ";
	public static final String WARNING_MAX_CRITICALITY_LEVEL = "Over maximum criticality level ";
	
	@Override
	public boolean validate(StructuralElementInstance sei) {
		boolean allInfoValid = true;
		BeanStructuralElementInstance beanSei = new BeanStructuralElementInstance();
		beanSei.setStructuralElementInstance(sei);
		
		ReliabilityAnalysis relAnalysis = beanSei.getFirst(ReliabilityAnalysis.class);
		MCSAnalysis cutSetAnalysis = beanSei.getFirst(MCSAnalysis.class);
		
		ReliabilityRequirement rr = beanSei.getFirst(ReliabilityRequirement.class);
		FaultToleranceRequirement ftr = beanSei.getFirst(FaultToleranceRequirement.class);
		
		Fault fault =  beanSei.getFirst(Fault.class);
		
		if (fault != null) {
			// check if the requirements for the fault scenario are fulfilled
			
			if (rr != null && relAnalysis != null) {
				if (rr.getMinReliability() > 0 && relAnalysis.getReliability() < rr.getMinReliability()) {
					allInfoValid = false;
					String fqn = rr.getMinReliabilityBean().getATypeInstance().getFullQualifiedInstanceName();
					IUuid iUuid = rr.getMinReliabilityBean().getTypeInstance();
																																	
					vfmHelper.createFDIRValidationMarker(IMarker.SEVERITY_WARNING, WARNING_MIN_RELIABILITY + fqn, iUuid);
				}
				
				/*
				if (rr.getMaxCriticality() > 0 && fault.getCriticalityLevel() > rr.getMaxCriticality()) {
					allInfoValid = false;
					String fqn = rr.getMaxCriticalityBean().getATypeInstance().getFullQualifiedInstanceName();
					IUuid iUuid = rr.getMaxCriticalityBean().getTypeInstance();
																																	
					vfmHelper.createFEAValidationMarker(IMarker.SEVERITY_WARNING, WARNING_MAX_CRITICALITY_LEVEL + fqn, iUuid);
				}
				*/
			}
			
			if (ftr != null && cutSetAnalysis != null) {
				if (ftr.getMinFaultTolerance() > 0 && cutSetAnalysis.getFaultTolerance() > 0 && cutSetAnalysis.getFaultTolerance() < ftr.getMinFaultTolerance()) {
					allInfoValid = false;
					String fqn = ftr.getMinFaultToleranceBean().getATypeInstance().getFullQualifiedInstanceName();
					IUuid iUuid = ftr.getMinFaultToleranceBean().getTypeInstance();
																																	
					vfmHelper.createFDIRValidationMarker(IMarker.SEVERITY_WARNING, WARNING_MIN_FAULT_TOLERANCE + fqn, iUuid);
				}
			}
		}
		
		return super.validate(sei) && allInfoValid;
	}
}
