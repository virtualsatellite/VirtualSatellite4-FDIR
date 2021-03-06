/**
 * generated by Xtext 2.22.0
 */
package de.dlr.sc.virsat.fdir.galileo.dft.util;

import de.dlr.sc.virsat.fdir.galileo.dft.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see de.dlr.sc.virsat.fdir.galileo.dft.DftPackage
 * @generated
 */
public class DftSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static DftPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DftSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = DftPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case DftPackage.GALILEO_DFT:
      {
        GalileoDft galileoDft = (GalileoDft)theEObject;
        T result = caseGalileoDft(galileoDft);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DftPackage.GALILEO_FAULT_TREE_NODE:
      {
        GalileoFaultTreeNode galileoFaultTreeNode = (GalileoFaultTreeNode)theEObject;
        T result = caseGalileoFaultTreeNode(galileoFaultTreeNode);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DftPackage.GALILEO_REPAIR_ACTION:
      {
        GalileoRepairAction galileoRepairAction = (GalileoRepairAction)theEObject;
        T result = caseGalileoRepairAction(galileoRepairAction);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DftPackage.GALILEO_NODE_TYPE:
      {
        GalileoNodeType galileoNodeType = (GalileoNodeType)theEObject;
        T result = caseGalileoNodeType(galileoNodeType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DftPackage.NAMED:
      {
        Named named = (Named)theEObject;
        T result = caseNamed(named);
        if (result == null) result = caseGalileoNodeType(named);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DftPackage.OBSERVER:
      {
        Observer observer = (Observer)theEObject;
        T result = caseObserver(observer);
        if (result == null) result = caseGalileoNodeType(observer);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case DftPackage.PARAMETRIZED:
      {
        Parametrized parametrized = (Parametrized)theEObject;
        T result = caseParametrized(parametrized);
        if (result == null) result = caseGalileoNodeType(parametrized);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Galileo Dft</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Galileo Dft</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGalileoDft(GalileoDft object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Galileo Fault Tree Node</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Galileo Fault Tree Node</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGalileoFaultTreeNode(GalileoFaultTreeNode object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Galileo Repair Action</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Galileo Repair Action</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGalileoRepairAction(GalileoRepairAction object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Galileo Node Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Galileo Node Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGalileoNodeType(GalileoNodeType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Named</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Named</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNamed(Named object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Observer</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Observer</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseObserver(Observer object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Parametrized</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Parametrized</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseParametrized(Parametrized object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //DftSwitch
