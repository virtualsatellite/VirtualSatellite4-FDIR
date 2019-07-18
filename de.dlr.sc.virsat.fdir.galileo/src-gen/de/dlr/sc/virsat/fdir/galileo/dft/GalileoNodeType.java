/**
 * generated by Xtext 2.12.0
 */
package de.dlr.sc.virsat.fdir.galileo.dft;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Galileo Node Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoNodeType#getTypeName <em>Type Name</em>}</li>
 *   <li>{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoNodeType#getObservables <em>Observables</em>}</li>
 *   <li>{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoNodeType#getObservationRate <em>Observation Rate</em>}</li>
 *   <li>{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoNodeType#getRateFactor <em>Rate Factor</em>}</li>
 *   <li>{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoNodeType#getTime <em>Time</em>}</li>
 * </ul>
 *
 * @see de.dlr.sc.virsat.fdir.galileo.dft.DftPackage#getGalileoNodeType()
 * @model
 * @generated
 */
public interface GalileoNodeType extends EObject
{
  /**
   * Returns the value of the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type Name</em>' attribute.
   * @see #setTypeName(String)
   * @see de.dlr.sc.virsat.fdir.galileo.dft.DftPackage#getGalileoNodeType_TypeName()
   * @model
   * @generated
   */
  String getTypeName();

  /**
   * Sets the value of the '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoNodeType#getTypeName <em>Type Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type Name</em>' attribute.
   * @see #getTypeName()
   * @generated
   */
  void setTypeName(String value);

  /**
   * Returns the value of the '<em><b>Observables</b></em>' reference list.
   * The list contents are of type {@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Observables</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Observables</em>' reference list.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.DftPackage#getGalileoNodeType_Observables()
   * @model
   * @generated
   */
  EList<GalileoFaultTreeNode> getObservables();

  /**
   * Returns the value of the '<em><b>Observation Rate</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Observation Rate</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Observation Rate</em>' attribute.
   * @see #setObservationRate(String)
   * @see de.dlr.sc.virsat.fdir.galileo.dft.DftPackage#getGalileoNodeType_ObservationRate()
   * @model
   * @generated
   */
  String getObservationRate();

  /**
   * Sets the value of the '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoNodeType#getObservationRate <em>Observation Rate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Observation Rate</em>' attribute.
   * @see #getObservationRate()
   * @generated
   */
  void setObservationRate(String value);

  /**
   * Returns the value of the '<em><b>Rate Factor</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Rate Factor</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Rate Factor</em>' attribute.
   * @see #setRateFactor(String)
   * @see de.dlr.sc.virsat.fdir.galileo.dft.DftPackage#getGalileoNodeType_RateFactor()
   * @model
   * @generated
   */
  String getRateFactor();

  /**
   * Sets the value of the '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoNodeType#getRateFactor <em>Rate Factor</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Rate Factor</em>' attribute.
   * @see #getRateFactor()
   * @generated
   */
  void setRateFactor(String value);

  /**
   * Returns the value of the '<em><b>Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Time</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Time</em>' attribute.
   * @see #setTime(String)
   * @see de.dlr.sc.virsat.fdir.galileo.dft.DftPackage#getGalileoNodeType_Time()
   * @model
   * @generated
   */
  String getTime();

  /**
   * Sets the value of the '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoNodeType#getTime <em>Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Time</em>' attribute.
   * @see #getTime()
   * @generated
   */
  void setTime(String value);

} // GalileoNodeType
