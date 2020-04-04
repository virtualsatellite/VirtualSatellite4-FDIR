/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram;

import java.util.Collection;

import org.eclipse.elk.alg.graphiti.GraphitiLayoutConfigurationStore;
import org.eclipse.elk.alg.graphiti.GraphitiLayoutSetup;
import org.eclipse.elk.core.service.ILayoutConfigurationStore;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.internal.parts.IPictogramElementEditPart;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

/**
 * This class extends the default GraphitiLayoutSetup to inject our own implementations.
 * @author muel_s8
 *
 */

@SuppressWarnings("restriction")
public abstract class VirSatGraphitiLayoutSetup extends GraphitiLayoutSetup {
	
	@Override
	public boolean supports(Object object) {
		
		Diagram diagram = null;
		
		// We need to figure out if this object is a graphiti diagram object and if so
		// to which diagram it belongs
		
		if (object instanceof Collection) {
			Collection<?> collection = (Collection<?>) object;
			for (Object o : collection) {
				if (o instanceof IPictogramElementEditPart) {
					IPictogramElementEditPart editPart = (IPictogramElementEditPart) o;
					diagram = Graphiti.getPeService().getDiagramForPictogramElement(editPart.getPictogramElement());
					break;
				} else if (o instanceof PictogramElement) {
					diagram = Graphiti.getPeService().getDiagramForPictogramElement((PictogramElement) o);
					break;
				}
			}
		} else {
			if (object instanceof IPictogramElementEditPart) {
				IPictogramElementEditPart editPart = (IPictogramElementEditPart) object;
				diagram = Graphiti.getPeService().getDiagramForPictogramElement(editPart.getPictogramElement());
			}

			if (object instanceof PictogramElement) {
				diagram = Graphiti.getPeService().getDiagramForPictogramElement((PictogramElement) object);
			}

			if (object instanceof DiagramEditor) {
				DiagramEditor editor = (DiagramEditor) object;
				diagram = editor.getDiagramTypeProvider().getDiagram();
			}
		}

		if (diagram != null) {
			return supports(diagram.getDiagramTypeId());
		}

		return false;
	}

	/**
	 * Checks if the diagram type is supported by this layout setup.
	 * Clients should override this. Returns always true per default.
	 * Note that if multiple layout setups are applicable the one with the higher
	 * priority defined in the respective extension point will be used.
	 * @param diagramType the diagram type
	 * @return true iff the diagram type is supported by this layout setup.
	 */
	protected abstract boolean supports(String diagramType);

	@Override
	public Injector createInjector(final Module defaultModule) {
		return Guice.createInjector(Modules.override(defaultModule).with(createLayoutModule()));
	}
	
	/**
	 * Override this class to create the coorect module
	 * @return the Layout module
	 */
	protected abstract LayoutModule createLayoutModule();

	/**
	 * Guice module for the VirSat Graphiti connector.
	 */
	public static class LayoutModule implements Module {
		@Override
		public void configure(final Binder binder) {
			binder.bind(ILayoutConfigurationStore.Provider.class).to(GraphitiLayoutConfigurationStore.Provider.class);
		}
	}
}
