/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.BeanDeleteFeature;
import de.dlr.sc.virsat.graphiti.ui.diagram.feature.BeanDirectEditNameFeature;
import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatChangeColorFeature;
import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatDiagramFeatureProvider;
import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirsatCategoryAssignmentOpenEditorFeature;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.NullObjectUpdateFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.comments.CommentAddFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.comments.CommentCreateFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.comments.CommentDirectEditFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.recoveryAutomata.MinimizeRAFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.recoveryAutomata.RecoveryAutomatonAddFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.states.StateAddFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.states.StateCreateFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.states.StateMoveFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.states.StateSetAsInitialStateFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.states.StateUnsetAsInitialStateFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.states.StateUpdateFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.transitions.FaultEventTransitionCreateFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.transitions.TimedTransitionCreateFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.transitions.TransitionAddFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.transitions.TransitionReconnectionFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.transitions.TransitionUpdateFeature;

/**
 * This feature provider provides all features for recovery automaton diagrams.
 * @author muel_s8
 *
 */

public class RecoveryAutomatonFeatureProvider extends VirSatDiagramFeatureProvider {
	
	/**
	 * Default constructor
	 * @param dtp the diagram type provider
	 */
	
	public RecoveryAutomatonFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
	}
	
	@Override
	public ICreateFeature[] getCreateFeatures() {
		List<ICreateFeature> createFeatures = new ArrayList<>();
		createFeatures.add(new CommentCreateFeature(this));
		createFeatures.add(new StateCreateFeature(this));
		return createFeatures.toArray(new ICreateFeature[createFeatures.size()]);
	}
	
	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		Object newObject = context.getNewObject();
		
		if (newObject instanceof CategoryAssignment) {
			CategoryAssignment ca = (CategoryAssignment) newObject;
			
			if (ca.getType().getName().equals(RecoveryAutomaton.class.getSimpleName())) {
				return new RecoveryAutomatonAddFeature(this);
			}
			
			if (ca.getType().getName().equals(State.class.getSimpleName())) {
				return new StateAddFeature(this);
			}
			
			if (ca.getType().getName().equals(Transition.class.getSimpleName())) {
				return new TransitionAddFeature(this);
			}
		}
		
		if (newObject instanceof RecoveryAutomaton) {
			return new RecoveryAutomatonAddFeature(this);
		}
		
		if (newObject instanceof State) {
			return new StateAddFeature(this);
		}
		
		if (newObject instanceof Transition) {
			return new TransitionAddFeature(this);
		}
		
		if (newObject instanceof String) {
			return new CommentAddFeature(this);
		}
		
		return super.getAddFeature(context);
	}
	
	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context) {
		PictogramElement pe = context.getPictogramElement();
		
		if (Graphiti.getPeService().getPropertyValue(pe, CommentAddFeature.IS_COMMENT_KEY) != null) {
			return null;
		}
		
		Object object = getBusinessObjectForPictogramElement(pe);
		
		if (Graphiti.getPeService().getPropertyValue(pe, StateAddFeature.IS_STATE_KEY) != null) {
			if (object == null) {
				return new NullObjectUpdateFeature(this);
			}
			
			return new StateUpdateFeature(this);
		}
		
		if (Graphiti.getPeService().getPropertyValue(pe, TransitionAddFeature.IS_TRANSITION_KEY) != null) {
			if (object == null) {
				return new NullObjectUpdateFeature(this);
			}
			
			return new TransitionUpdateFeature(this);
		}
				
		return super.getUpdateFeature(context);
	}
	
	@Override
	public IDeleteFeature getDeleteFeature(IDeleteContext context) {
		Object object = getBusinessObjectForPictogramElement(context.getPictogramElement());
		
		if (object instanceof RecoveryAutomaton || object instanceof State || object instanceof Transition) {
			return new BeanDeleteFeature(this);
		}
		
		return super.getDeleteFeature(context);
	}
	
	@Override
	public IRemoveFeature getRemoveFeature(IRemoveContext context) {
		return super.getRemoveFeature(context);
	}
	
	@Override
	public ILayoutFeature getLayoutFeature(ILayoutContext context) {
		return super.getLayoutFeature(context);
	}
	
	@Override
	public IDirectEditingFeature getDirectEditingFeature(IDirectEditingContext context) {
		if (Graphiti.getPeService().getPropertyValue(context.getPictogramElement(), CommentAddFeature.IS_COMMENT_KEY) != null) {
			return new CommentDirectEditFeature(this);
		}
		
		Object object = getBusinessObjectForPictogramElement(context.getPictogramElement());
		
		if (object instanceof State) {
			return new BeanDirectEditNameFeature(this);
		}
		
		return super.getDirectEditingFeature(context);
	}
	
	@Override
	public ICreateConnectionFeature[] getCreateConnectionFeatures() {
		return new ICreateConnectionFeature[] { new FaultEventTransitionCreateFeature(this), new TimedTransitionCreateFeature(this) };
	}
	
	@Override
	public IReconnectionFeature getReconnectionFeature(IReconnectionContext context) {
		Object object = getBusinessObjectForPictogramElement(context.getConnection());
		
		if (object instanceof Transition) {
			return new TransitionReconnectionFeature(this);
		}
		
		return super.getReconnectionFeature(context);
	}
	
	@Override
	public ICustomFeature[] getCustomFeatures(ICustomContext context) {
		PictogramElement[] pe = context.getPictogramElements();
		Object object = getBusinessObjectForPictogramElement(pe[0]);
		if (object instanceof State) {
			State state = (State) object;
			RecoveryAutomaton ra = state.getParentCaBeanOfClass(RecoveryAutomaton.class);
			State initialState = ra.getInitial();
			if (initialState == null || !(initialState.equals(state))) {
				return new ICustomFeature[] { new VirsatCategoryAssignmentOpenEditorFeature(this),
					new VirSatChangeColorFeature(this), new StateSetAsInitialStateFeature(this),
					new MinimizeRAFeature(this) };
			} else {
				return new ICustomFeature[] { new VirsatCategoryAssignmentOpenEditorFeature(this),
					new VirSatChangeColorFeature(this), new StateUnsetAsInitialStateFeature(this),
					new MinimizeRAFeature(this) };
			}
		}

		return new ICustomFeature[] { new VirsatCategoryAssignmentOpenEditorFeature(this),
			new VirSatChangeColorFeature(this) };
	}

	@Override
	public IMoveShapeFeature getMoveShapeFeature(IMoveShapeContext context) {
		Object object = getBusinessObjectForPictogramElement(context.getPictogramElement());
		
		if (object instanceof State) {
			return new StateMoveFeature(this);
		}
		
		return super.getMoveShapeFeature(context);
	}
}
