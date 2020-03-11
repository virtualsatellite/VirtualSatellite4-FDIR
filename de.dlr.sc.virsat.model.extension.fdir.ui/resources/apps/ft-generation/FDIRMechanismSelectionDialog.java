import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

import de.dlr.sc.virsat.apps.api.external.ModelAPI;
import de.dlr.sc.virsat.model.concept.types.IBeanName;
import de.dlr.sc.virsat.model.concept.types.category.IBeanCategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.concept.types.structural.IBeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.ps.model.ConfigurationTree;
import de.dlr.sc.virsat.model.extension.ps.model.ElementDefinition;
import de.dlr.sc.virsat.model.extension.ps.model.ProductTree;
import de.dlr.sc.virsat.model.extension.ps.model.ProductTreeDomain;

public class FDIRMechanismSelectionDialog extends ElementTreeSelectionDialog {

	private ModelAPI modelAPI;
	private Map<Fault, Set<ElementDefinition>> mapFaultToFDIRMechanism = new HashMap<>();
	private Fault selectedFault;
	
	public static FDIRMechanismSelectionDialog create(ModelAPI modelAPI, ConfigurationTree root) {
		ITreeContentProvider fContentProvider = new ITreeContentProvider() {
			@Override
			public boolean hasChildren(Object arg0) {
				return getChildren(arg0).length > 0;
			}
			
			@Override
			public Object getParent(Object arg0) {
				if (arg0 instanceof IBeanStructuralElementInstance) {
					return ((IBeanStructuralElementInstance) arg0).getParentSeiBean();
				} else if (arg0 instanceof IBeanCategoryAssignment) {
					StructuralElementInstance parentSei = (StructuralElementInstance) ((IBeanCategoryAssignment) arg0).getTypeInstance().eContainer();
					return new BeanStructuralElementInstance(parentSei);
				}
				
				return null;
			}
			
			@Override
			public Object[] getElements(Object arg0) {
				return getChildren(arg0);
			}
			
			@Override
			public Object[] getChildren(Object arg0) {
				if (arg0 instanceof IBeanStructuralElementInstance) {
					List<Object> children = new ArrayList<>();
					IBeanStructuralElementInstance bean = (IBeanStructuralElementInstance) arg0;
					children.addAll(bean.getAll(Fault.class));
					children.addAll(bean.getChildren(IBeanStructuralElementInstance.class));
					return children.toArray(new Object[0]);
				}
				return new Object[0];
			}
		};
		
		ILabelProvider fLabelProvider = new LabelProvider() {
			public String getText(Object element) {
				return ((IBeanName) element).getName();
			};
		};
		
		return new FDIRMechanismSelectionDialog(modelAPI, root, fLabelProvider, fContentProvider);
	}
	
	public FDIRMechanismSelectionDialog(ModelAPI modelAPI, ConfigurationTree root, ILabelProvider fLabelProvider, ITreeContentProvider fContentProvider) {
		super(null, fLabelProvider, fContentProvider);
		this.modelAPI = modelAPI;
		setTitle("Configure FDIR Mechanisms");
		setInput(root);	
	}

	@Override
	protected Label createMessageArea(Composite composite) {
		return null;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, true);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite treeArea = (Composite) super.createDialogArea(composite);	
		GridLayout treeLayout = (GridLayout) treeArea.getLayout();
		treeLayout.marginHeight = 0;
		treeLayout.marginTop = 0;
		
		ProductTreeDomain fdirToolbox = modelAPI.getRootSeis(ProductTree.class).get(0).getChildren(ProductTreeDomain.class).get(0);
		
		CheckboxTableViewer tableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER);
		Table tableWidget = tableViewer.getTable();	
		tableWidget.setLayoutData(getTreeViewer().getTree().getLayoutData());
		tableViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public Object[] getElements(Object arg0) {
				return fdirToolbox.getChildren(ElementDefinition.class).toArray();
			}
		});
		tableViewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				return ((IBeanName) element).getName();
			};
		});
		tableViewer.setInput(fdirToolbox);
		
		getTreeViewer().addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object selectedElement = selection.getFirstElement();
				if (selectedElement instanceof Fault) {
					selectedFault = (Fault) selectedElement;
				} else {
					selectedFault = null;
				}
				
				if (selectedFault != null) {
					mapFaultToFDIRMechanism.computeIfAbsent(selectedFault, v -> new HashSet<>());
				}
				tableViewer.getTable().setEnabled(selectedFault != null);
				tableViewer.refresh();
			}
		});
		
		tableViewer.setCheckStateProvider(new ICheckStateProvider() {
			@Override
			public boolean isGrayed(Object arg0) {
				return false;
			}
			
			@Override
			public boolean isChecked(Object arg0) {
				return selectedFault != null && mapFaultToFDIRMechanism.get(selectedFault).contains(arg0);
			}
		});
		
		tableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				if (!event.getChecked()) {
					mapFaultToFDIRMechanism.get(selectedFault).remove(event.getElement());
				} else {
					mapFaultToFDIRMechanism.get(selectedFault).add((ElementDefinition) event.getElement());
				}
			}
		});
		
		return composite;
	}
	
	public Map<Fault, Set<ElementDefinition>> getMapFaultToFDIRMechanism() {
		return mapFaultToFDIRMechanism;
	}
}
