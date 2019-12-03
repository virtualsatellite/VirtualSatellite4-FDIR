package de.dlr.sc.virsat.model.extension.fdir.validator;

import org.eclipse.core.resources.IMarker;

import de.dlr.sc.virsat.model.dvlm.general.IUuid;
import de.dlr.sc.virsat.model.extension.fdir.marker.VirSatFDIRMarkerHelper;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;

public class FaultValidator {
	
	public static final String WARNING_MISSING_FTEFROM = "FaultTreeEdgeFrom is not set for FaultTreeEdge ";
	public static final String WARNING_MISSING_FTETO = "FaultTreeEdgeTo is not set for FaultTreeEdge ";
	
	private VirSatFDIRMarkerHelper vfmHelper;

	/**
	 * Standard constructor.
	 * @param vfmHelper helper to manage problem markers
	 */
	public FaultValidator(VirSatFDIRMarkerHelper vfmHelper) {
		this.vfmHelper = vfmHelper;
	}

	/**
	 * Performs a validation of a single fault
	 * @param fault the fault
	 * @return true iff there are no problems
	 */
	public boolean validate(Fault fault) {
		boolean allInfoValid = true;
		
		FaultTreeHelper ftHelper = new FaultTreeHelper(fault.getConcept());
		for (FaultTreeEdge fte : ftHelper.getEdges(fault)) {
			if (fte.getFrom() == null) {
				allInfoValid = false;
				String fqn = fte.getTypeInstance().getFullQualifiedInstanceName();
				IUuid iUuid = fte.getFromReferenceProperty();
				vfmHelper.createFDIRValidationMarker(IMarker.SEVERITY_WARNING, WARNING_MISSING_FTEFROM + fqn, iUuid);
			}
			
			if (fte.getTo() == null) {
				allInfoValid = false;
				String fqn = fte.getTypeInstance().getFullQualifiedInstanceName();
				IUuid iUuid = fte.getToReferenceProperty();
				vfmHelper.createFDIRValidationMarker(IMarker.SEVERITY_WARNING, WARNING_MISSING_FTETO + fqn, iUuid);
			}
		}
		
		return allInfoValid;
	}
}
