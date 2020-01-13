/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveAnchorFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IMoveAnchorContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.BeanDirectEditNameFeature;
import de.dlr.sc.virsat.graphiti.ui.diagram.feature.BeanPropertyFloatDirectEditValueFeature;
import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatChangeColorFeature;
import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatDiagramFeatureProvider;
import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirsatCategoryAssignmentOpenEditorFeature;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import de.dlr.sc.virsat.model.extension.fdir.model.AbstractFaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.RDEP;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.NullObjectUpdateFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.comments.CommentAddFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.comments.CommentCreateFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.comments.CommentDirectEditFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.basicEvents.BasicEventAddFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.basicEvents.BasicEventRemoveFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.basicEvents.BasicEventsConnectionAddFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.basicEvents.BasicEventsCreateFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.basicEvents.BasicEventsLayoutFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.basicEvents.BasicEventsUpdateFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.connections.AbstractConnectionUpdateFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.connections.ConnectionAddFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.connections.ConnectionDeleteFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.connections.ConnectionReconnectionFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.connections.PropagationCreateFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes.FaultTreeNodeAddFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes.FaultTreeNodeCollapseFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes.FaultTreeNodeCreateFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes.FaultTreeNodeDeleteFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes.FaultTreeNodeLayoutFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes.FaultTreeNodeMoveFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes.FaultTreeNodeUpdateFeature;

/**
 * This feature provider provides all features for fault tree diagrams.
 * @author muel_s8
 *
 */

public class FaultTreeFeatureProvider extends VirSatDiagramFeatureProvider {
	
	/**
	 * Default constructor
	 * @param dtp the diagram type provider
	 */
	
	public FaultTreeFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
	}
	
	@Override
	public ICreateFeature[] getCreateFeatures() {
		List<ICreateFeature> createFeatures = new ArrayList<>();
		createFeatures.add(new CommentCreateFeature(this));
		
		for (FaultTreeNodeType type : FaultTreeNodeType.values()) {
			if (type != FaultTreeNodeType.BASIC_EVENT) {
				createFeatures.add(new FaultTreeNodeCreateFeature(this, type));
			}
		}
		
		createFeatures.add(new BasicEventsCreateFeature(this));
		return createFeatures.toArray(new ICreateFeature[createFeatures.size()]);
	}
	
	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		Object newObject = context.getNewObject();
		
		if (newObject instanceof CategoryAssignment) {
			CategoryAssignment ca = (CategoryAssignment) newObject;
			
			if (ca.getType().getName().equals(BasicEvent.class.getSimpleName())) {
				return new BasicEventAddFeature(this);
			}
			
			Set<String> extendedCategoryNames = ActiveConceptHelper.getAllNames(ca.getType());
			if (extendedCategoryNames.contains(FaultTreeNode.class.getSimpleName())) {
				return new FaultTreeNodeAddFeature(this);
			}
		}
		
		if (newObject instanceof BasicEvent) {
			return new BasicEventsConnectionAddFeature(this);
		}
		
		if (newObject instanceof AbstractFaultTreeEdge) {
			return new ConnectionAddFeature(this);
		}
		
		if (newObject instanceof String) {
			return new CommentAddFeature(this);
		}
		
		return super.getAddFeature(context);
	}
	
	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();
		
		if (Graphiti.getPeService().getPropertyValue(pictogramElement, CommentAddFeature.IS_COMMENT_KEY) != null) {
			return null;
		}
		
		Object object = getBusinessObjectForPictogramElement(pictogramElement);
		
		if (object == null && pictogramElement instanceof ContainerShape && !(pictogramElement instanceof Diagram)) {
			return new NullObjectUpdateFeature(this);
		}

		if (object instanceof BasicEvent) {
			return new BasicEventsUpdateFeature(this);
		}
		
		if (object instanceof FaultTreeNode) {
			return new FaultTreeNodeUpdateFeature(this);
		}
		
		if ((object instanceof FaultTreeEdge || object == null) && pictogramElement instanceof FreeFormConnection) {
			return new AbstractConnectionUpdateFeature(this);
		}
		
		return super.getUpdateFeature(context);
	}
	
	@Override
	public IDeleteFeature getDeleteFeature(IDeleteContext context) {
		Object object = getBusinessObjectForPictogramElement(context.getPictogramElement());
		
		if (object instanceof AbstractFaultTreeEdge) {
			return new ConnectionDeleteFeature(this);
		}
		
		if (object instanceof FaultTreeNode) {
			// Cant delete the containment between basic event and fault
			if (object instanceof BasicEvent && context.getPictogramElement() instanceof Connection) {
				return null;
			}
			
			return new FaultTreeNodeDeleteFeature(this);
		}
		
		return super.getDeleteFeature(context);
	}
	
	@Override
	public IRemoveFeature getRemoveFeature(IRemoveContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();
		Object object = getBusinessObjectForPictogramElement(pictogramElement);
		
		if (object instanceof BasicEvent) {
			if (pictogramElement instanceof FreeFormConnection && ((FreeFormConnection) pictogramElement).getStart() != null) {
				return null;
			} else {
				return new BasicEventRemoveFeature(this);
			}
		}
		
		return super.getRemoveFeature(context);
	}
	
	@Override
	public ILayoutFeature getLayoutFeature(ILayoutContext context) {
		Object object = getBusinessObjectForPictogramElement(context.getPictogramElement());
		
		if (object instanceof BasicEvent) {
			return new BasicEventsLayoutFeature(this);
		}
		
		if (object instanceof FaultTreeNode) {
			return new FaultTreeNodeLayoutFeature(this);
		}
		
		return super.getLayoutFeature(context);
	}
	
	@Override
	public IDirectEditingFeature getDirectEditingFeature(IDirectEditingContext context) {
		if (Graphiti.getPeService().getPropertyValue(context.getPictogramElement(), CommentAddFeature.IS_COMMENT_KEY) != null) {
			return new CommentDirectEditFeature(this);
		}
		
		Object object = getBusinessObjectForPictogramElement(context.getPictogramElement());
		
		if (object instanceof FaultTreeNode) {
			return new BeanDirectEditNameFeature(this);
		}
		
		if (object instanceof BeanPropertyFloat) {
			BeanPropertyFloat beanProperty = (BeanPropertyFloat) object;
			String propName = beanProperty.getATypeInstance().getType().getName();
			if (propName.equals(BasicEvent.PROPERTY_HOTFAILURERATE) || propName.equals(RDEP.PROPERTY_RATECHANGE)) {
				return new BeanPropertyFloatDirectEditValueFeature(this);
			}
		}
		
		return super.getDirectEditingFeature(context);
	}
	
	@Override
	public ICreateConnectionFeature[] getCreateConnectionFeatures() {
		return new ICreateConnectionFeature[] { new PropagationCreateFeature(this) };
	}
	
	@Override
	public IReconnectionFeature getReconnectionFeature(IReconnectionContext context) {
		Object object = getBusinessObjectForPictogramElement(context.getConnection());
		
		if (object instanceof AbstractFaultTreeEdge || object instanceof BasicEvent) {
			return new ConnectionReconnectionFeature(this);
		}
		
		return super.getReconnectionFeature(context);
	}
	
	@Override
	public ICustomFeature[] getCustomFeatures(ICustomContext context) {
		PictogramElement[] pe = context.getPictogramElements();
		Object object = getBusinessObjectForPictogramElement(pe[0]);
		if (object instanceof Fault) {
			return new ICustomFeature[] { new VirsatCategoryAssignmentOpenEditorFeature(this), new VirSatChangeColorFeature(this), new FaultTreeNodeCollapseFeature(this)};
		}
		
		return new ICustomFeature[] { new VirsatCategoryAssignmentOpenEditorFeature(this), new VirSatChangeColorFeature(this)};
	} 
	
	@Override
	public IMoveAnchorFeature getMoveAnchorFeature(IMoveAnchorContext context) {
		return null;
	}
	
	@Override
	public IResizeShapeFeature getResizeShapeFeature(IResizeShapeContext context) {
		return null;
	}
	
	@Override
	public IMoveShapeFeature getMoveShapeFeature(IMoveShapeContext context) {
		Object object = getBusinessObjectForPictogramElement(context.getPictogramElement());
		
		if (object instanceof FaultTreeNode) {
			return new FaultTreeNodeMoveFeature(this);
		}
		
		return super.getMoveShapeFeature(context);
	}
}
