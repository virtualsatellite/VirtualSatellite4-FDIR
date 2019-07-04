/**
 * This file is part of the VirSat project.
 *
 * Copyright (c) 2008-2016
 * German Aerospace Center (DLR), Simulation and Software Technology, Germany
 * All rights reserved
 *
 */
package de.dlr.sc.virsat.model.extension.fdir.ui.snippet;

import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.uiengine.ui.editor.snippets.IUiSnippet;
import de.dlr.sc.virsat.uiengine.ui.editor.snippets.AUiSnippetArrayInstancePropertyTable;
import de.dlr.sc.virsat.model.extension.fdir.ui.command.CreateAddArrayElementAvailabilityCurveCommand;


/**
 * Auto Generated Abstract Generator Gap Class
 * 
 * Don't Manually modify this class
 * 
 * 
 * 
 */	
public abstract class AUiSnippetTableAvailabilityAnalysisAvailabilityCurve extends AUiSnippetArrayInstancePropertyTable implements IUiSnippet {

	public AUiSnippetTableAvailabilityAnalysisAvailabilityCurve() {
		super("de.dlr.sc.virsat.model.extension.fdir",
			"availabilityCurve",
			"AvailabilityAnalysis",
			STYLE_ADD_BUTTON | STYLE_REMOVE_BUTTON | STYLE_EDITOR_BUTTON);
	}

	@Override
	protected Command createAddCommand(EditingDomain editingDomain, Concept activeConcept) {
		return new CreateAddArrayElementAvailabilityCurveCommand().create(editingDomain, getArrayInstance(model), null);
	}
}
