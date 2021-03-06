/**
 * generated by Xtext 2.22.0
 */
package de.dlr.sc.virsat.fdir.galileo.dft.util;

import de.dlr.sc.virsat.fdir.galileo.dft.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see de.dlr.sc.virsat.fdir.galileo.dft.DftPackage
 * @generated
 */
public class DftAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static DftPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DftAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = DftPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DftSwitch<Adapter> modelSwitch =
    new DftSwitch<Adapter>()
    {
      @Override
      public Adapter caseGalileoDft(GalileoDft object)
      {
        return createGalileoDftAdapter();
      }
      @Override
      public Adapter caseGalileoFaultTreeNode(GalileoFaultTreeNode object)
      {
        return createGalileoFaultTreeNodeAdapter();
      }
      @Override
      public Adapter caseGalileoRepairAction(GalileoRepairAction object)
      {
        return createGalileoRepairActionAdapter();
      }
      @Override
      public Adapter caseGalileoNodeType(GalileoNodeType object)
      {
        return createGalileoNodeTypeAdapter();
      }
      @Override
      public Adapter caseNamed(Named object)
      {
        return createNamedAdapter();
      }
      @Override
      public Adapter caseObserver(Observer object)
      {
        return createObserverAdapter();
      }
      @Override
      public Adapter caseParametrized(Parametrized object)
      {
        return createParametrizedAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object)
      {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft <em>Galileo Dft</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft
   * @generated
   */
  public Adapter createGalileoDftAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode <em>Galileo Fault Tree Node</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode
   * @generated
   */
  public Adapter createGalileoFaultTreeNodeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoRepairAction <em>Galileo Repair Action</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoRepairAction
   * @generated
   */
  public Adapter createGalileoRepairActionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.dlr.sc.virsat.fdir.galileo.dft.GalileoNodeType <em>Galileo Node Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.GalileoNodeType
   * @generated
   */
  public Adapter createGalileoNodeTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.dlr.sc.virsat.fdir.galileo.dft.Named <em>Named</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.Named
   * @generated
   */
  public Adapter createNamedAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.dlr.sc.virsat.fdir.galileo.dft.Observer <em>Observer</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.Observer
   * @generated
   */
  public Adapter createObserverAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.dlr.sc.virsat.fdir.galileo.dft.Parametrized <em>Parametrized</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.dlr.sc.virsat.fdir.galileo.dft.Parametrized
   * @generated
   */
  public Adapter createParametrizedAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} //DftAdapterFactory
