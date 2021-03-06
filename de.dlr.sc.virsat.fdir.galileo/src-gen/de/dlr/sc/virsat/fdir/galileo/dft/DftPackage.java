/**
 * generated by Xtext 2.22.0
 */
package de.dlr.sc.virsat.fdir.galileo.dft;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see de.dlr.sc.virsat.fdir.galileo.dft.DftFactory
 * @model kind="package"
 * @generated
 */
public interface DftPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "dft";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.dlr.de/sc/virsat/fdir/galileo/Dft";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "dft";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  DftPackage eINSTANCE = de.dlr.sc.virsat.fdir.galileo.dft.impl.DftPackageImpl.init();

  /**
   * The meta object id for the '{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoDftImpl <em>Galileo Dft</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoDftImpl
   * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.DftPackageImpl#getGalileoDft()
   * @generated
   */
  int GALILEO_DFT = 0;

  /**
   * The feature id for the '<em><b>Root</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_DFT__ROOT = 0;

  /**
   * The feature id for the '<em><b>Gates</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_DFT__GATES = 1;

  /**
   * The feature id for the '<em><b>Basic Events</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_DFT__BASIC_EVENTS = 2;

  /**
   * The number of structural features of the '<em>Galileo Dft</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_DFT_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoFaultTreeNodeImpl <em>Galileo Fault Tree Node</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoFaultTreeNodeImpl
   * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.DftPackageImpl#getGalileoFaultTreeNode()
   * @generated
   */
  int GALILEO_FAULT_TREE_NODE = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_FAULT_TREE_NODE__NAME = 0;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_FAULT_TREE_NODE__TYPE = 1;

  /**
   * The feature id for the '<em><b>Children</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_FAULT_TREE_NODE__CHILDREN = 2;

  /**
   * The feature id for the '<em><b>Lambda</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_FAULT_TREE_NODE__LAMBDA = 3;

  /**
   * The feature id for the '<em><b>Prob</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_FAULT_TREE_NODE__PROB = 4;

  /**
   * The feature id for the '<em><b>Dorm</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_FAULT_TREE_NODE__DORM = 5;

  /**
   * The feature id for the '<em><b>Repair Actions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_FAULT_TREE_NODE__REPAIR_ACTIONS = 6;

  /**
   * The number of structural features of the '<em>Galileo Fault Tree Node</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_FAULT_TREE_NODE_FEATURE_COUNT = 7;

  /**
   * The meta object id for the '{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoRepairActionImpl <em>Galileo Repair Action</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoRepairActionImpl
   * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.DftPackageImpl#getGalileoRepairAction()
   * @generated
   */
  int GALILEO_REPAIR_ACTION = 2;

  /**
   * The feature id for the '<em><b>Repair</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_REPAIR_ACTION__REPAIR = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_REPAIR_ACTION__NAME = 1;

  /**
   * The feature id for the '<em><b>Observartions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_REPAIR_ACTION__OBSERVARTIONS = 2;

  /**
   * The number of structural features of the '<em>Galileo Repair Action</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_REPAIR_ACTION_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoNodeTypeImpl <em>Galileo Node Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoNodeTypeImpl
   * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.DftPackageImpl#getGalileoNodeType()
   * @generated
   */
  int GALILEO_NODE_TYPE = 3;

  /**
   * The number of structural features of the '<em>Galileo Node Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GALILEO_NODE_TYPE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.NamedImpl <em>Named</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.NamedImpl
   * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.DftPackageImpl#getNamed()
   * @generated
   */
  int NAMED = 4;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAMED__TYPE_NAME = GALILEO_NODE_TYPE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Named</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAMED_FEATURE_COUNT = GALILEO_NODE_TYPE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.ObserverImpl <em>Observer</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.ObserverImpl
   * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.DftPackageImpl#getObserver()
   * @generated
   */
  int OBSERVER = 5;

  /**
   * The feature id for the '<em><b>Observables</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OBSERVER__OBSERVABLES = GALILEO_NODE_TYPE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Observation Rate</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OBSERVER__OBSERVATION_RATE = GALILEO_NODE_TYPE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Observer</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OBSERVER_FEATURE_COUNT = GALILEO_NODE_TYPE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.ParametrizedImpl <em>Parametrized</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.ParametrizedImpl
   * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.DftPackageImpl#getParametrized()
   * @generated
   */
  int PARAMETRIZED = 6;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETRIZED__TYPE_NAME = GALILEO_NODE_TYPE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Parameter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETRIZED__PARAMETER = GALILEO_NODE_TYPE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Parametrized</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETRIZED_FEATURE_COUNT = GALILEO_NODE_TYPE_FEATURE_COUNT + 2;


  /**
   * Returns the meta object for class '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft <em>Galileo Dft</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Galileo Dft</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft
   * @generated
   */
  EClass getGalileoDft();

  /**
   * Returns the meta object for the reference '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft#getRoot <em>Root</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Root</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft#getRoot()
   * @see #getGalileoDft()
   * @generated
   */
  EReference getGalileoDft_Root();

  /**
   * Returns the meta object for the containment reference list '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft#getGates <em>Gates</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Gates</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft#getGates()
   * @see #getGalileoDft()
   * @generated
   */
  EReference getGalileoDft_Gates();

  /**
   * Returns the meta object for the containment reference list '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft#getBasicEvents <em>Basic Events</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Basic Events</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft#getBasicEvents()
   * @see #getGalileoDft()
   * @generated
   */
  EReference getGalileoDft_BasicEvents();

  /**
   * Returns the meta object for class '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode <em>Galileo Fault Tree Node</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Galileo Fault Tree Node</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode
   * @generated
   */
  EClass getGalileoFaultTreeNode();

  /**
   * Returns the meta object for the attribute '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode#getName()
   * @see #getGalileoFaultTreeNode()
   * @generated
   */
  EAttribute getGalileoFaultTreeNode_Name();

  /**
   * Returns the meta object for the containment reference '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode#getType()
   * @see #getGalileoFaultTreeNode()
   * @generated
   */
  EReference getGalileoFaultTreeNode_Type();

  /**
   * Returns the meta object for the reference list '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode#getChildren <em>Children</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Children</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode#getChildren()
   * @see #getGalileoFaultTreeNode()
   * @generated
   */
  EReference getGalileoFaultTreeNode_Children();

  /**
   * Returns the meta object for the attribute '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode#getLambda <em>Lambda</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Lambda</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode#getLambda()
   * @see #getGalileoFaultTreeNode()
   * @generated
   */
  EAttribute getGalileoFaultTreeNode_Lambda();

  /**
   * Returns the meta object for the attribute '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode#getProb <em>Prob</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Prob</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode#getProb()
   * @see #getGalileoFaultTreeNode()
   * @generated
   */
  EAttribute getGalileoFaultTreeNode_Prob();

  /**
   * Returns the meta object for the attribute '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode#getDorm <em>Dorm</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Dorm</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode#getDorm()
   * @see #getGalileoFaultTreeNode()
   * @generated
   */
  EAttribute getGalileoFaultTreeNode_Dorm();

  /**
   * Returns the meta object for the containment reference list '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode#getRepairActions <em>Repair Actions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Repair Actions</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode#getRepairActions()
   * @see #getGalileoFaultTreeNode()
   * @generated
   */
  EReference getGalileoFaultTreeNode_RepairActions();

  /**
   * Returns the meta object for class '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoRepairAction <em>Galileo Repair Action</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Galileo Repair Action</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoRepairAction
   * @generated
   */
  EClass getGalileoRepairAction();

  /**
   * Returns the meta object for the attribute '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoRepairAction#getRepair <em>Repair</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Repair</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoRepairAction#getRepair()
   * @see #getGalileoRepairAction()
   * @generated
   */
  EAttribute getGalileoRepairAction_Repair();

  /**
   * Returns the meta object for the attribute '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoRepairAction#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoRepairAction#getName()
   * @see #getGalileoRepairAction()
   * @generated
   */
  EAttribute getGalileoRepairAction_Name();

  /**
   * Returns the meta object for the reference list '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoRepairAction#getObservartions <em>Observartions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Observartions</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoRepairAction#getObservartions()
   * @see #getGalileoRepairAction()
   * @generated
   */
  EReference getGalileoRepairAction_Observartions();

  /**
   * Returns the meta object for class '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoNodeType <em>Galileo Node Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Galileo Node Type</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoNodeType
   * @generated
   */
  EClass getGalileoNodeType();

  /**
   * Returns the meta object for class '{@link de.dlr.sc.virsat.fdir.galileo.dft.Named <em>Named</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Named</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.Named
   * @generated
   */
  EClass getNamed();

  /**
   * Returns the meta object for the attribute '{@link de.dlr.sc.virsat.fdir.galileo.dft.Named#getTypeName <em>Type Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type Name</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.Named#getTypeName()
   * @see #getNamed()
   * @generated
   */
  EAttribute getNamed_TypeName();

  /**
   * Returns the meta object for class '{@link de.dlr.sc.virsat.fdir.galileo.dft.Observer <em>Observer</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Observer</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.Observer
   * @generated
   */
  EClass getObserver();

  /**
   * Returns the meta object for the reference list '{@link de.dlr.sc.virsat.fdir.galileo.dft.Observer#getObservables <em>Observables</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Observables</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.Observer#getObservables()
   * @see #getObserver()
   * @generated
   */
  EReference getObserver_Observables();

  /**
   * Returns the meta object for the attribute '{@link de.dlr.sc.virsat.fdir.galileo.dft.Observer#getObservationRate <em>Observation Rate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Observation Rate</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.Observer#getObservationRate()
   * @see #getObserver()
   * @generated
   */
  EAttribute getObserver_ObservationRate();

  /**
   * Returns the meta object for class '{@link de.dlr.sc.virsat.fdir.galileo.dft.Parametrized <em>Parametrized</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Parametrized</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.Parametrized
   * @generated
   */
  EClass getParametrized();

  /**
   * Returns the meta object for the attribute '{@link de.dlr.sc.virsat.fdir.galileo.dft.Parametrized#getTypeName <em>Type Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type Name</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.Parametrized#getTypeName()
   * @see #getParametrized()
   * @generated
   */
  EAttribute getParametrized_TypeName();

  /**
   * Returns the meta object for the attribute '{@link de.dlr.sc.virsat.fdir.galileo.dft.Parametrized#getParameter <em>Parameter</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Parameter</em>'.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.Parametrized#getParameter()
   * @see #getParametrized()
   * @generated
   */
  EAttribute getParametrized_Parameter();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  DftFactory getDftFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoDftImpl <em>Galileo Dft</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoDftImpl
     * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.DftPackageImpl#getGalileoDft()
     * @generated
     */
    EClass GALILEO_DFT = eINSTANCE.getGalileoDft();

    /**
     * The meta object literal for the '<em><b>Root</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GALILEO_DFT__ROOT = eINSTANCE.getGalileoDft_Root();

    /**
     * The meta object literal for the '<em><b>Gates</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GALILEO_DFT__GATES = eINSTANCE.getGalileoDft_Gates();

    /**
     * The meta object literal for the '<em><b>Basic Events</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GALILEO_DFT__BASIC_EVENTS = eINSTANCE.getGalileoDft_BasicEvents();

    /**
     * The meta object literal for the '{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoFaultTreeNodeImpl <em>Galileo Fault Tree Node</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoFaultTreeNodeImpl
     * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.DftPackageImpl#getGalileoFaultTreeNode()
     * @generated
     */
    EClass GALILEO_FAULT_TREE_NODE = eINSTANCE.getGalileoFaultTreeNode();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GALILEO_FAULT_TREE_NODE__NAME = eINSTANCE.getGalileoFaultTreeNode_Name();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GALILEO_FAULT_TREE_NODE__TYPE = eINSTANCE.getGalileoFaultTreeNode_Type();

    /**
     * The meta object literal for the '<em><b>Children</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GALILEO_FAULT_TREE_NODE__CHILDREN = eINSTANCE.getGalileoFaultTreeNode_Children();

    /**
     * The meta object literal for the '<em><b>Lambda</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GALILEO_FAULT_TREE_NODE__LAMBDA = eINSTANCE.getGalileoFaultTreeNode_Lambda();

    /**
     * The meta object literal for the '<em><b>Prob</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GALILEO_FAULT_TREE_NODE__PROB = eINSTANCE.getGalileoFaultTreeNode_Prob();

    /**
     * The meta object literal for the '<em><b>Dorm</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GALILEO_FAULT_TREE_NODE__DORM = eINSTANCE.getGalileoFaultTreeNode_Dorm();

    /**
     * The meta object literal for the '<em><b>Repair Actions</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GALILEO_FAULT_TREE_NODE__REPAIR_ACTIONS = eINSTANCE.getGalileoFaultTreeNode_RepairActions();

    /**
     * The meta object literal for the '{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoRepairActionImpl <em>Galileo Repair Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoRepairActionImpl
     * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.DftPackageImpl#getGalileoRepairAction()
     * @generated
     */
    EClass GALILEO_REPAIR_ACTION = eINSTANCE.getGalileoRepairAction();

    /**
     * The meta object literal for the '<em><b>Repair</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GALILEO_REPAIR_ACTION__REPAIR = eINSTANCE.getGalileoRepairAction_Repair();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GALILEO_REPAIR_ACTION__NAME = eINSTANCE.getGalileoRepairAction_Name();

    /**
     * The meta object literal for the '<em><b>Observartions</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GALILEO_REPAIR_ACTION__OBSERVARTIONS = eINSTANCE.getGalileoRepairAction_Observartions();

    /**
     * The meta object literal for the '{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoNodeTypeImpl <em>Galileo Node Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoNodeTypeImpl
     * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.DftPackageImpl#getGalileoNodeType()
     * @generated
     */
    EClass GALILEO_NODE_TYPE = eINSTANCE.getGalileoNodeType();

    /**
     * The meta object literal for the '{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.NamedImpl <em>Named</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.NamedImpl
     * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.DftPackageImpl#getNamed()
     * @generated
     */
    EClass NAMED = eINSTANCE.getNamed();

    /**
     * The meta object literal for the '<em><b>Type Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute NAMED__TYPE_NAME = eINSTANCE.getNamed_TypeName();

    /**
     * The meta object literal for the '{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.ObserverImpl <em>Observer</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.ObserverImpl
     * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.DftPackageImpl#getObserver()
     * @generated
     */
    EClass OBSERVER = eINSTANCE.getObserver();

    /**
     * The meta object literal for the '<em><b>Observables</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OBSERVER__OBSERVABLES = eINSTANCE.getObserver_Observables();

    /**
     * The meta object literal for the '<em><b>Observation Rate</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute OBSERVER__OBSERVATION_RATE = eINSTANCE.getObserver_ObservationRate();

    /**
     * The meta object literal for the '{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.ParametrizedImpl <em>Parametrized</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.ParametrizedImpl
     * @see de.dlr.sc.virsat.fdir.galileo.dft.impl.DftPackageImpl#getParametrized()
     * @generated
     */
    EClass PARAMETRIZED = eINSTANCE.getParametrized();

    /**
     * The meta object literal for the '<em><b>Type Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAMETRIZED__TYPE_NAME = eINSTANCE.getParametrized_TypeName();

    /**
     * The meta object literal for the '<em><b>Parameter</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAMETRIZED__PARAMETER = eINSTANCE.getParametrized_Parameter();

  }

} //DftPackage
