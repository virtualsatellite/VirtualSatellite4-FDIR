/**
 * generated by Xtext 2.22.0
 */
package de.dlr.sc.virsat.fdir.galileo.dft.impl;

import de.dlr.sc.virsat.fdir.galileo.dft.DftPackage;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoRepairAction;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Galileo Repair Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoRepairActionImpl#getRepair <em>Repair</em>}</li>
 *   <li>{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoRepairActionImpl#getName <em>Name</em>}</li>
 *   <li>{@link de.dlr.sc.virsat.fdir.galileo.dft.impl.GalileoRepairActionImpl#getObservartions <em>Observartions</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GalileoRepairActionImpl extends MinimalEObjectImpl.Container implements GalileoRepairAction
{
  /**
   * The default value of the '{@link #getRepair() <em>Repair</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRepair()
   * @generated
   * @ordered
   */
  protected static final String REPAIR_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRepair() <em>Repair</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRepair()
   * @generated
   * @ordered
   */
  protected String repair = REPAIR_EDEFAULT;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getObservartions() <em>Observartions</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getObservartions()
   * @generated
   * @ordered
   */
  protected EList<GalileoFaultTreeNode> observartions;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected GalileoRepairActionImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return DftPackage.Literals.GALILEO_REPAIR_ACTION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getRepair()
  {
    return repair;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setRepair(String newRepair)
  {
    String oldRepair = repair;
    repair = newRepair;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DftPackage.GALILEO_REPAIR_ACTION__REPAIR, oldRepair, repair));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DftPackage.GALILEO_REPAIR_ACTION__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<GalileoFaultTreeNode> getObservartions()
  {
    if (observartions == null)
    {
      observartions = new EObjectResolvingEList<GalileoFaultTreeNode>(GalileoFaultTreeNode.class, this, DftPackage.GALILEO_REPAIR_ACTION__OBSERVARTIONS);
    }
    return observartions;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case DftPackage.GALILEO_REPAIR_ACTION__REPAIR:
        return getRepair();
      case DftPackage.GALILEO_REPAIR_ACTION__NAME:
        return getName();
      case DftPackage.GALILEO_REPAIR_ACTION__OBSERVARTIONS:
        return getObservartions();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case DftPackage.GALILEO_REPAIR_ACTION__REPAIR:
        setRepair((String)newValue);
        return;
      case DftPackage.GALILEO_REPAIR_ACTION__NAME:
        setName((String)newValue);
        return;
      case DftPackage.GALILEO_REPAIR_ACTION__OBSERVARTIONS:
        getObservartions().clear();
        getObservartions().addAll((Collection<? extends GalileoFaultTreeNode>)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case DftPackage.GALILEO_REPAIR_ACTION__REPAIR:
        setRepair(REPAIR_EDEFAULT);
        return;
      case DftPackage.GALILEO_REPAIR_ACTION__NAME:
        setName(NAME_EDEFAULT);
        return;
      case DftPackage.GALILEO_REPAIR_ACTION__OBSERVARTIONS:
        getObservartions().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case DftPackage.GALILEO_REPAIR_ACTION__REPAIR:
        return REPAIR_EDEFAULT == null ? repair != null : !REPAIR_EDEFAULT.equals(repair);
      case DftPackage.GALILEO_REPAIR_ACTION__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case DftPackage.GALILEO_REPAIR_ACTION__OBSERVARTIONS:
        return observartions != null && !observartions.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (repair: ");
    result.append(repair);
    result.append(", name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //GalileoRepairActionImpl
